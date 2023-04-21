package cu.ftpd.filesystem.permissions;

import cu.ftpd.user.User;
import cu.ftpd.logging.Formatter;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Markus Jevring <markus@jevring.net>
 * @since 2007-jul-21 : 21:37:38
 * @version $Id: Permissions.java 307 2011-02-27 19:10:52Z jevring $
 */
public class Permissions {

    private final LinkedList<ActionPermission> permissions = new LinkedList<ActionPermission>();

    private final LinkedList<VisibilityPermission> visibilityPermissions = new LinkedList<VisibilityPermission>();

    private final LinkedList<SpeedPermission> speedPermissions = new LinkedList<SpeedPermission>();

    private final HashMap<String, String> aliases = new HashMap<String, String>();

    private final File permissionFile;

    private long loadTime;

    private static final String matchEntity = "(.+?|\\$.+?|\\{.+\\})";

    private static final String dirOrAlias = "(\\$.+?|\".+\"|\\{.+\\})";

    private static final String location = "\\s*(?:in\\s+" + dirOrAlias + ")";

    private static final String time = "(\\d{2}:\\d{2})";

    private static final String users = "\\s*(group\\:.+?|user\\:.+?|\\$.+?|\\{.+\\})";

    private static final String subjects = "\\s*(file\\:.+?|dir\\:.+?|group\\:.+?|user\\:.+?|\\$.+?|\\{.+\\})";

    private static final String timespan = "\\s*(?:between " + time + " and " + time + ")";

    private static final String quick = "\\s*(quick)?";

    private static final Pattern allow = Pattern.compile("allow" + quick + "\\s*" + matchEntity + location + "?\\s*(?:by\\s+(.+))?");

    private static final Pattern deny = Pattern.compile("deny" + quick + "\\s*" + matchEntity + location + "?\\s*(?:by\\s+(.+))?");

    private static final Pattern hide = Pattern.compile("hide" + quick + subjects + location + "?\\s+from\\s+" + users);

    private static final Pattern show = Pattern.compile("show" + quick + subjects + location + "?\\s+to\\s+" + users);

    private static final Pattern restrict = Pattern.compile("restrict" + quick + "\\s*" + users + "?" + location + "?" + timespan + "?\\s*to (.+)");

    private static final Pattern unrestrict = Pattern.compile("unrestrict" + quick + "\\s*" + users + "?" + location + "?" + timespan + "?");

    private static final Pattern speed = Pattern.compile("(\\d+(?:k|K|m|M)? up)?\\s*(?:and)?\\s*(\\d+(?:k|K|m|M)? down)?");

    public Permissions(File file) throws IOException, PermissionConfigurationException {
        permissionFile = file;
        readFile(permissionFile);
        loadTime = System.currentTimeMillis();
    }

    private void readFile(File file) throws IOException, PermissionConfigurationException {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;
            int type;
            boolean quick;
            int lineNumber = 0;
            while ((line = in.readLine()) != null) {
                lineNumber++;
                if (!line.startsWith("#") && !"".equals(line)) {
                    if (line.startsWith("include")) {
                        readFile(new File(permissionFile.getParentFile(), line.substring(8).trim()));
                        continue;
                    }
                    Matcher matcher;
                    if ((matcher = allow.matcher(line)).matches()) {
                        type = Permission.ALLOW;
                    } else if ((matcher = deny.matcher(line)).matches()) {
                        type = Permission.DENY;
                    } else if ((matcher = hide.matcher(line)).matches()) {
                        type = Permission.HIDE;
                    } else if ((matcher = show.matcher(line)).matches()) {
                        type = Permission.SHOW;
                    } else if ((matcher = restrict.matcher(line)).matches()) {
                        type = Permission.RESTRICT;
                    } else if ((matcher = unrestrict.matcher(line)).matches()) {
                        type = Permission.UNRESTRICT;
                    } else {
                        createAlias(line, lineNumber);
                        continue;
                    }
                    quick = "quick".equalsIgnoreCase(matcher.group(1));
                    try {
                        if (type == Permission.ALLOW || type == Permission.DENY) {
                            parsePermission(type, quick, matcher);
                        } else if (type == Permission.HIDE || type == Permission.SHOW) {
                            parseVisibilityPermission(type, quick, matcher);
                        } else if (type == Permission.RESTRICT || type == Permission.UNRESTRICT) {
                            parseSpeedPermission(type, quick, matcher);
                        }
                    } catch (PermissionConfigurationException e) {
                        e.setLine(line);
                        e.setLineNumber(lineNumber);
                        throw e;
                    }
                }
            }
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }

    private void createAlias(String line, int lineNumber) throws PermissionConfigurationException {
        if (line.contains("=")) {
            String[] s = line.split("=", 2);
            String key = s[0].trim();
            if (ActionPermission.isReservedWord(key)) {
                throw new PermissionConfigurationException("Cannot use reserved words as aliases: " + key);
            }
            aliases.put('$' + key, s[1].trim());
        } else {
            throw new PermissionConfigurationException("Found invalid rule");
        }
    }

    private void parsePermission(int type, boolean quick, Matcher matcher) throws UnknownPermissionException, UnknownParameterException {
        ArrayList<String> paths = parsePaths(resolveAlias(matcher.group(3)));
        ArrayList<Integer> permissions = parsePermissions(resolveAlias(matcher.group(2)));
        ActionPermission p;
        for (Integer pe : permissions) {
            for (String pa : paths) {
                p = new ActionPermission(type, pe, pa, quick);
                setUsersAndGroups(resolveAlias(matcher.group(4)), p);
                this.permissions.add(p);
            }
        }
    }

    private void parseVisibilityPermission(int type, boolean quick, Matcher matcher) {
        ArrayList<String> paths = parsePaths(resolveAlias(matcher.group(3)));
        VisibilityPermission vp;
        for (String pa : paths) {
            vp = new VisibilityPermission(type, quick, pa);
            setSubjects(resolveAlias(matcher.group(2)), vp);
            setObservers(resolveAlias(matcher.group(4)), vp);
            visibilityPermissions.add(vp);
        }
    }

    private void parseSpeedPermission(int type, boolean quick, Matcher matcher) throws MissingPermissionParameterException, UnknownParameterException {
        TimeOfDay start = null;
        TimeOfDay end = null;
        if (matcher.group(4) != null && matcher.group(5) != null) {
            start = getTimeOfDay(matcher.group(4));
            end = getTimeOfDay(matcher.group(5));
        }
        if (matcher.group(3) != null) {
            ArrayList<String> paths = parsePaths(resolveAlias(matcher.group(3)));
            for (String path : paths) {
                createSpeedPermission(type, quick, matcher, start, end, path);
            }
        } else {
            createSpeedPermission(type, quick, matcher, start, end, null);
        }
    }

    private void createSpeedPermission(int type, boolean quick, Matcher matcher, TimeOfDay start, TimeOfDay end, String path) throws MissingPermissionParameterException, UnknownParameterException {
        SpeedPermission sp;
        sp = new SpeedPermission(type, path, quick, start, end);
        setUsersAndGroups(resolveAlias(matcher.group(2)), sp);
        if (type == SpeedPermission.RESTRICT) {
            setSpeed(matcher.group(6), sp);
        }
        this.speedPermissions.add(sp);
    }

    private void setSpeed(String value, SpeedPermission sp) throws MissingPermissionParameterException {
        if (value != null) {
            boolean atLeastOneSpeedSet = false;
            Matcher m = speed.matcher(value);
            if (m.matches()) {
                long speed;
                String up = m.group(1);
                if (up != null) {
                    speed = Formatter.size(up.substring(0, up.indexOf(' ')));
                    sp.setUploadRestriction(speed);
                    atLeastOneSpeedSet = true;
                }
                String down = m.group(2);
                if (down != null) {
                    speed = Formatter.size(down.substring(0, down.indexOf(' ')));
                    sp.setDownloadRestriction(speed);
                    atLeastOneSpeedSet = true;
                }
                if (!atLeastOneSpeedSet) {
                    throw new MissingPermissionParameterException("speed");
                }
            } else {
                throw new MissingPermissionParameterException("speed");
            }
        } else {
            throw new MissingPermissionParameterException("speed");
        }
    }

    private TimeOfDay getTimeOfDay(String time) {
        String[] t = time.split(":");
        return new TimeOfDay(Integer.parseInt(t[0]), Integer.parseInt(t[1]));
    }

    private void setObservers(String observers, VisibilityPermission vp) {
        if (observers.charAt(0) == '{') {
            String[] entities = observers.substring(1, observers.length() - 1).split("\\s+|,");
            for (String entity : entities) {
                addObserver(entity, vp);
            }
        } else {
            addObserver(observers, vp);
        }
    }

    private void addObserver(String entity, VisibilityPermission vp) {
        if (entity.startsWith("user:")) {
            vp.addObserverUser(entity.substring(5));
        } else if (entity.startsWith("group:")) {
            vp.addObserverGroup(entity.substring(6));
        }
    }

    private void setSubjects(String subjects, VisibilityPermission vp) {
        if (subjects.charAt(0) == '{') {
            String[] entities = subjects.substring(1, subjects.length() - 1).split("\\s+|,");
            for (String entity : entities) {
                addSubjects(entity, vp);
            }
        } else {
            addSubjects(subjects, vp);
        }
    }

    private void addSubjects(String entity, VisibilityPermission vp) {
        if (entity.startsWith("user:")) {
            vp.addSubjectUser(entity.substring(5));
        } else if (entity.startsWith("group:")) {
            vp.addSubjectGroup(entity.substring(6));
        } else if (entity.startsWith("file:")) {
            vp.addSubjectFile(entity.substring(5));
        } else if (entity.startsWith("dir:")) {
            vp.addSubjectDirectory(entity.substring(4));
        }
    }

    private ArrayList<String> parsePaths(String path) {
        ArrayList<String> paths = new ArrayList<String>();
        if (path == null) {
            paths.add(null);
        } else if (path.charAt(0) == '{') {
            String[] s = path.substring(1, path.length() - 1).split("\\s+|,");
            for (int i = 0; i < s.length; i++) {
                String p = s[i];
                if (p.length() > 0) {
                    paths.add(p.substring(1, p.length() - 1));
                }
            }
        } else {
            String s = path;
            if (s.charAt(0) == '"') {
                s = s.substring(1, s.length() - 1);
            }
            paths.add(s);
        }
        return paths;
    }

    private ArrayList<Integer> parsePermissions(String permission) throws UnknownPermissionException {
        ArrayList<Integer> permissions = new ArrayList<Integer>();
        if (permission.charAt(0) == '{') {
            String[] s = permission.substring(1, permission.length() - 1).split("\\s+|,");
            for (int i = 0; i < s.length; i++) {
                String perm = s[i];
                if (perm.length() > 0) {
                    Integer p = ActionPermission.resolve(perm);
                    permissions.add(p);
                }
            }
        } else {
            Integer p = ActionPermission.resolve(permission);
            permissions.add(p);
        }
        return permissions;
    }

    /**
     * Resolves an alias if it can, otherwise returns the alias.
     * @param alias the alias to be resolved.
     * @return the resolved alias if resolution wasn possible, otherwise just the alias.
     */
    private String resolveAlias(String alias) {
        String a = aliases.get(alias);
        if (a != null) {
            return a;
        } else {
            return alias;
        }
    }

    private void setUsersAndGroups(String usersOrGroups, UserAndGroupPermission permission) throws UnknownParameterException {
        if (usersOrGroups != null) {
            if (usersOrGroups.charAt(0) == '{') {
                if (usersOrGroups.endsWith("}")) {
                    String[] entities = usersOrGroups.substring(1, usersOrGroups.length() - 1).split("\\s+|,");
                    for (String entity : entities) {
                        if (entity.length() > 0) {
                            addEntityToPermission(entity, permission);
                        }
                    }
                } else {
                    throw new UnknownParameterException("This line contains data that should not be there: " + usersOrGroups);
                }
            } else {
                addEntityToPermission(usersOrGroups, permission);
            }
        } else {
            permission.addGroup("*");
            permission.addUser("*");
        }
    }

    private void addEntityToPermission(String entity, UserAndGroupPermission permission) throws UnknownParameterException {
        if (entity.startsWith("user:")) {
            permission.addUser(entity.substring(5));
        } else if (entity.startsWith("group:")) {
            permission.addGroup(entity.substring(6));
        } else {
            throw new UnknownParameterException("Found entity that was neither user nor group: " + entity);
        }
    }

    /**
     * Determines if the observer can see the subject in the specified directory.
     * The path, if provided, MUST be a directory that is absolute, but rooted in the ftp root.
     * See permissions.acl on how this matching should be done (we will have a boolean value for the permission that will
     * change back and forth for each (applicable) rule that is matched until we reach the end or a rule has the 'quick' tag)
     * 
     * @param observer the user who wants to see
     * @param subject the user who might be hidden. (if this is null, it refers to any user)
     * @param path the path, must be a directory.
     * @return true if the observer can see the subject in the path, false otherwise.
     */
    public boolean canSee(User observer, User subject, String path) {
        boolean canSee = true;
        for (VisibilityPermission vp : visibilityPermissions) {
            if (path == null || vp.getDirectoryPattern().matcher(path).matches()) {
                if ((vp.appliesToObserverUser(observer.getUsername()) || vp.appliesToObserverGroups(observer.getGroups())) && (subject == null || vp.appliesToSubjectUser(subject.getUsername()) || vp.appliesToSubjectGroups(subject.getGroups()))) {
                    canSee = (vp.getVisibility() == VisibilityPermission.SHOW);
                    if (vp.isQuick()) {
                        break;
                    }
                }
            }
        }
        return canSee;
    }

    public boolean isVisible(User observer, String filename, String path, boolean isDirectory) {
        boolean isVisible = true;
        for (VisibilityPermission vp : visibilityPermissions) {
            if (path == null || vp.getDirectory() == null || vp.getDirectoryPattern().matcher(path).matches()) {
                if (vp.appliesToObserverUser(observer.getUsername()) || vp.appliesToObserverGroups(observer.getGroups())) {
                    if (isDirectory) {
                        if (vp.appliesToSubjectDirectory(filename)) {
                            isVisible = (vp.getVisibility() == VisibilityPermission.SHOW);
                            if (vp.isQuick()) {
                                break;
                            }
                        }
                    } else {
                        if (vp.appliesToSubjectFile(filename)) {
                            isVisible = (vp.getVisibility() == VisibilityPermission.SHOW);
                            if (vp.isQuick()) {
                                break;
                            }
                        }
                    }
                }
            }
        }
        return isVisible;
    }

    /**
     * See permissions.acl on how this matching should be done (we will have a boolean value for the permission that will
     * change back and forth for each (applicable) rule that is matched until we reach the end or a rule has the 'quick' tag)
     * The path, if provided, MUST be a directory that is absolute, but rooted in the ftp root.
     *
     * @param permission the permission to check for.
     * @param path make sure the path we get here is the directory in question, and not the path of a whole file. This path has to be an ftp-path, rather than a "real" path.
     * @param user the user for which to check the permission
     * @return true if the specified user has the specified permission in the specified path
     */
    public boolean hasPermission(int permission, String path, User user) {
        boolean hasPermission = true;
        for (ActionPermission p : permissions) {
            if (path == null || p.getDirectory() == null || p.getDirectoryPattern().matcher(path).matches()) {
                if (p.appliesToGroups(user.getGroups()) || p.appliesToUser(user.getUsername())) {
                    if (p.getPermission() == permission || p.getPermission() == ActionPermission.ID_ANY) {
                        if (p.getType() == Permission.ALLOW) {
                            hasPermission = true;
                        } else if (p.getType() == Permission.DENY) {
                            hasPermission = false;
                        }
                        if (p.isQuick()) {
                            break;
                        }
                    }
                }
            }
        }
        return hasPermission;
    }

    public boolean shouldLog(int loggingPermission, String path, User user) {
        return hasPermission(loggingPermission, path, user);
    }

    /**
     * Queries the permission system to determine the speed limit, if any, for the directory, time and user in question.
     * The path, if provided, MUST be a directory that is absolute, but rooted in the ftp root.
     *
     * @param user the user we want to check for.
     * @param path the path in which we want to check. can be null.
     * @param direction the direction we want the limit for, SpeedPermission.{UP, DOWN}
     * @return the limit if there was one, 0 if no applicable rule could be found.
     */
    public long getLimit(User user, String path, int direction) {
        TimeOfDay now = new TimeOfDay(System.currentTimeMillis());
        long limit = 0;
        for (SpeedPermission sp : speedPermissions) {
            if (path == null || sp.getDirectory() == null || sp.getDirectoryPattern().matcher(path).matches()) {
                if (sp.appliesToGroups(user.getGroups()) || sp.appliesToUser(user.getUsername())) {
                    if (sp.appliesToTime(now)) {
                        if (sp.getType() == SpeedPermission.RESTRICT) {
                            limit = sp.getLimit(direction);
                        } else {
                            limit = 0;
                        }
                        if (sp.isQuick()) {
                            break;
                        }
                    }
                }
            }
        }
        return limit;
    }

    public long getLoadTime() {
        return loadTime;
    }

    public static void main(String[] args) {
        try {
            Permissions ps = new Permissions(new File("data/permissions.acl"));
            for (Permission p : ps.permissions) {
                System.out.println(p);
            }
            for (VisibilityPermission vp : ps.visibilityPermissions) {
                System.out.println(vp);
            }
            for (SpeedPermission sp : ps.speedPermissions) {
                System.out.println(sp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (PermissionConfigurationException e) {
            e.printStackTrace();
        }
    }
}
