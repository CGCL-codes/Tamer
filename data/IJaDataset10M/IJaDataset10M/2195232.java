package libomv.examples.TestClient;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import libomv.AgentManager.InstantMessageCallbackArgs;
import libomv.AgentManager.InstantMessageDialog;
import libomv.GridClient;
import libomv.GroupManager.CurrentGroupsCallbackArgs;
import libomv.GroupManager.Group;
import libomv.GroupManager.GroupMember;
import libomv.GroupManager.GroupMembersReplyCallbackArgs;
import libomv.LoginManager.LoginProgressCallbackArgs;
import libomv.LoginManager.LoginStatus;
import libomv.Settings;
import libomv.Simulator;
import libomv.inventory.InventoryFolder;
import libomv.inventory.InventoryManager.InventoryObjectOfferedCallbackArgs;
import libomv.packets.AgentDataUpdatePacket;
import libomv.packets.AlertMessagePacket;
import libomv.packets.AvatarAppearancePacket;
import libomv.packets.Packet;
import libomv.packets.PacketType;
import libomv.types.PacketCallback;
import libomv.types.UUID;
import libomv.utils.Callback;
import libomv.utils.Helpers;
import libomv.utils.Logger;
import libomv.utils.Logger.LogLevel;
import libomv.utils.TimeoutEvent;

public class TestClient extends GridClient implements PacketCallback {

    public UUID GroupID = UUID.Zero;

    public HashMap<UUID, GroupMember> GroupMembers;

    public HashMap<UUID, AvatarAppearancePacket> Appearances = new HashMap<UUID, AvatarAppearancePacket>();

    public HashMap<String, Command> Commands = new HashMap<String, Command>();

    public boolean Running = true;

    public boolean GroupCommands = false;

    public String MasterName = Helpers.EmptyString;

    public UUID MasterKey = UUID.Zero;

    public boolean AllowObjectMaster = false;

    public ClientManager _ClientManager;

    public InventoryFolder CurrentDirectory = null;

    private Timer updateTimer;

    private UUID GroupMembersRequestID;

    public HashMap<UUID, Group> GroupsCache = null;

    private TimeoutEvent<Boolean> GroupsEvent = new TimeoutEvent<Boolean>();

    public TestClient(ClientManager manager, Settings settings) throws Exception {
        super(settings);
        _ClientManager = manager;
        RegisterAllCommands(this.getClass());
        Logger.LOG_LEVEL = LogLevel.Warning;
        Settings.LOG_RESENDS = false;
        Settings.STORE_LAND_PATCHES = true;
        Settings.ALWAYS_DECODE_OBJECTS = true;
        Settings.ALWAYS_REQUEST_OBJECTS = true;
        Settings.SEND_AGENT_UPDATES = true;
        Settings.USE_ASSET_CACHE = true;
        Login.OnLoginProgress.add(new LoginHandler(), false);
        Self.OnInstantMessage.add(new Self_IM(), false);
        Groups.OnGroupMembersReply.add(new GroupMembersHandler(), false);
        Inventory.OnInventoryObjectOffered.add(new Inventory_OnInventoryObjectReceived(), false);
        Network.RegisterCallback(PacketType.AgentDataUpdate, this);
        Network.RegisterCallback(PacketType.AvatarAppearance, this);
        Network.RegisterCallback(PacketType.AlertMessage, this);
        updateTimer = new Timer();
        updateTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                for (Command c : Commands.values()) {
                    if (c.Active) c.Think();
                }
            }
        }, 500, 500);
    }

    @Override
    public void packetCallback(Packet packet, Simulator simulator) throws Exception {
        switch(packet.getType()) {
            case AgentDataUpdate:
                AgentDataUpdateHandler(packet, simulator);
                break;
            case AvatarAppearance:
                AvatarAppearanceHandler(packet, simulator);
                break;
            case AlertMessage:
                AlertMessageHandler(packet, simulator);
                break;
        }
    }

    private class Self_IM implements Callback<InstantMessageCallbackArgs> {

        @Override
        public boolean callback(InstantMessageCallbackArgs e) {
            boolean groupIM = e.getIM().GroupIM && GroupMembers != null && GroupMembers.containsKey(e.getIM().FromAgentID) ? true : false;
            if (e.getIM().FromAgentID.equals(MasterKey) || (GroupCommands && groupIM)) {
                System.out.println(String.format("<%s (%s)> %s: %s (@{4}:{5})", e.getIM().GroupIM ? "GroupIM" : "IM", e.getIM().Dialog, e.getIM().FromAgentName, e.getIM().Message, e.getIM().RegionID, e.getIM().Position));
                try {
                    if (e.getIM().Dialog == InstantMessageDialog.RequestTeleport) {
                        System.out.println("Accepting teleport lure.");
                        Self.TeleportLureRespond(e.getIM().FromAgentID, e.getIM().IMSessionID, true);
                    } else if (e.getIM().Dialog == InstantMessageDialog.MessageFromAgent || e.getIM().Dialog == InstantMessageDialog.MessageFromObject) {
                        ClientManager.getInstance().DoCommandAll(e.getIM().Message, e.getIM().FromAgentID);
                    }
                } catch (Exception ex) {
                }
            } else {
                System.out.println(String.format("<%s (%s)> %s (not master): %s (@%s:%s)", e.getIM().GroupIM ? "GroupIM" : "IM", e.getIM().Dialog, e.getIM().FromAgentName, e.getIM().Message, e.getIM().RegionID, e.getIM().Position));
            }
            return false;
        }
    }

    /**
	 * Initialize everything that needs to be initialized once we're logged in.
	 *
	 * @param login The status of the login
	 * @param message Error message on failure, MOTD on success.
	 */
    private class LoginHandler implements Callback<LoginProgressCallbackArgs> {

        @Override
        public boolean callback(LoginProgressCallbackArgs e) {
            if (e.getStatus() == LoginStatus.Success) {
                CurrentDirectory = Inventory.getRootNode(true);
            }
            return false;
        }
    }

    public void RegisterAllCommands(Class<?> assembly) {
        ArrayList<Class<?>> classes = getDerivedClasses(assembly.getPackage(), Command.class);
        for (Class<?> clazz : classes) {
            Class<?> supClass = clazz.getSuperclass();
            try {
                if (supClass != null && supClass.equals(Command.class)) {
                    @SuppressWarnings("unchecked") Constructor<Command> ctor = (Constructor<Command>) clazz.getDeclaredConstructor(new Class[] { TestClient.class });
                    Command command = ctor.newInstance(new Object[] { this });
                    RegisterCommand(command);
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
    }

    public void RegisterCommand(Command command) {
        command.Client = this;
        if (!Commands.containsKey(command.Name.toLowerCase())) {
            Commands.put(command.Name.toLowerCase(), command);
        }
    }

    public void ReloadGroupsCache() throws Exception {
        Callback<CurrentGroupsCallbackArgs> currentGroups = new Groups_CurrentGroups();
        Groups.OnCurrentGroups.add(currentGroups);
        Groups.RequestCurrentGroups();
        GroupsEvent.waitOne(10000);
        Groups.OnCurrentGroups.remove(currentGroups);
        GroupsEvent.reset();
    }

    private class Groups_CurrentGroups implements Callback<CurrentGroupsCallbackArgs> {

        @Override
        public boolean callback(CurrentGroupsCallbackArgs e) {
            if (null == GroupsCache) GroupsCache = e.getGroups(); else {
                synchronized (GroupsCache) {
                    GroupsCache = e.getGroups();
                }
            }
            GroupsEvent.set(true);
            return false;
        }
    }

    private class GroupMembersHandler implements Callback<GroupMembersReplyCallbackArgs> {

        @Override
        public boolean callback(GroupMembersReplyCallbackArgs e) {
            if (e.getRequestID().equals(GroupMembersRequestID)) {
                GroupMembers = e.getMembers();
            }
            return false;
        }
    }

    private class Inventory_OnInventoryObjectReceived implements Callback<InventoryObjectOfferedCallbackArgs> {

        @Override
        public boolean callback(InventoryObjectOfferedCallbackArgs e) {
            if (MasterKey != UUID.Zero) {
                if (e.getOffer().FromAgentID != MasterKey) return false;
            } else if (GroupMembers != null && !GroupMembers.containsKey(e.getOffer().FromAgentID)) {
                return false;
            }
            e.setAccept(true);
            return false;
        }
    }

    public UUID GroupName2UUID(String groupName) throws Exception {
        UUID tryUUID = UUID.Parse(groupName);
        if (tryUUID != null) return tryUUID;
        if (null == GroupsCache) {
            ReloadGroupsCache();
            if (null == GroupsCache) return UUID.Zero;
        }
        synchronized (GroupsCache) {
            if (GroupsCache.size() > 0) {
                for (Group currentGroup : GroupsCache.values()) if (currentGroup.getName().equalsIgnoreCase(groupName)) return currentGroup.getID();
            }
        }
        return UUID.Zero;
    }

    private void AgentDataUpdateHandler(Packet packet, Simulator simulator) throws Exception {
        AgentDataUpdatePacket p = (AgentDataUpdatePacket) packet;
        if (p.AgentData.AgentID.equals(simulator.getClient().Self.getAgentID())) {
            GroupID = p.AgentData.ActiveGroupID;
            GroupMembersRequestID = simulator.getClient().Groups.RequestGroupMembers(GroupID);
        }
    }

    private void AvatarAppearanceHandler(Packet packet, Simulator simulator) {
        AvatarAppearancePacket appearance = (AvatarAppearancePacket) packet;
        synchronized (Appearances) {
            Appearances.put(appearance.Sender.ID, appearance);
        }
    }

    private void AlertMessageHandler(Packet packet, Simulator simulator) throws UnsupportedEncodingException {
        AlertMessagePacket message = (AlertMessagePacket) packet;
        Logger.Log("[AlertMessage] " + Helpers.BytesToString(message.AlertData.getMessage()), LogLevel.Info, this);
    }

    private static ArrayList<Class<?>> getDerivedClasses(Package pkg, Class<?> superClazz) {
        String pkgname = pkg.getName();
        ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
        File directory = null;
        String relPath = pkgname.replace('.', '/');
        URL resource = ClassLoader.getSystemClassLoader().getResource(relPath);
        if (resource == null) {
            throw new RuntimeException("No resource for " + relPath);
        }
        try {
            directory = new File(resource.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(pkgname + " (" + resource + ") does not appear to be a valid URL / URI.  Strange, since we got it from the system...", e);
        } catch (IllegalArgumentException e) {
            directory = null;
        }
        if (directory != null && directory.exists()) {
            enumerateAllDerivedClasses(classes, pkgname, directory, superClazz);
        } else {
            try {
                String fullPath = resource.getFile();
                String jarPath = fullPath.replaceFirst("[.]jar[!].*", ".jar").replaceFirst("file:", "");
                JarFile jarFile = new JarFile(jarPath);
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    String entryName = entry.getName();
                    if (entryName.startsWith(relPath) && entryName.length() > (relPath.length() + "/".length())) {
                        String className = entryName.replace('/', '.').replace('\\', '.').replace(".class", "");
                        try {
                            Class<?> clazz = Class.forName(className);
                            if (superClazz.equals(clazz.getSuperclass())) classes.add(clazz);
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException("ClassNotFoundException loading " + className);
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(pkgname + " (" + directory + ") does not appear to be a valid package", e);
            }
        }
        return classes;
    }

    private static void enumerateAllDerivedClasses(ArrayList<Class<?>> classes, String pkgname, File directory, Class<?> superClazz) {
        File[] files = directory.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                enumerateAllDerivedClasses(classes, pkgname + "." + files[i].getName(), files[i], superClazz);
            } else {
                String fileName = files[i].getName();
                if (fileName.endsWith(".class")) {
                    String className = pkgname + '.' + fileName.substring(0, fileName.length() - 6);
                    try {
                        Class<?> clazz = Class.forName(className);
                        if (superClazz.equals(clazz.getSuperclass())) classes.add(clazz);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException("ClassNotFoundException loading " + className);
                    }
                }
            }
        }
    }
}
