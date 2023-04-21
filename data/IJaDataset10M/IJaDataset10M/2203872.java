package logic;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import misc.Env;
import misc.NetworkClass;
import misc.ShellEnv;
import domain.Node;
import domain.Share;

public class NodeFactory extends ItemFactory implements Observer {

    protected static final String LOCALNETWORKADDRESS = "127.0.0.0";

    protected static final String LOCALHOSTADDRESS = "127.0.0.1";

    public NodeFactory(Property property) {
        super(property);
        m_ItemHandler = new NodeHandler();
        m_ItemHandler.addObserver(this);
    }

    public void update() {
    }

    @SuppressWarnings("unchecked")
    public void update(KeyAction keyAction, HashMap<String, Object> keyValuePairs) {
        Node host;
        HashMap<String, Object> tempHostValues;
        ArrayList<Share> tempShareValues;
        ArrayList<Share> shares;
        for (Object obj1 : keyValuePairs.keySet()) {
            tempHostValues = (HashMap<String, Object>) keyValuePairs.get(obj1);
            tempShareValues = (ArrayList<Share>) tempHostValues.get("shares");
            shares = new ArrayList<Share>();
            for (Share obj2 : tempShareValues) {
                shares.add(obj2);
            }
            host = new Node(Integer.valueOf((String) obj1).intValue(), (String) tempHostValues.get("name"), (String) tempHostValues.get("ipaddress"), shares);
            host.setUsername((String) tempHostValues.get("username"));
            host.setPassword((String) tempHostValues.get("password"));
            m_Objects.put(Integer.valueOf(obj1 + "").intValue() + "", host);
        }
    }

    public ArrayList<Node> scan() {
        boolean isAnalyzed, isShareColumn;
        int networkInterfaceCounter, index;
        String[] networkInterface, networkAddress;
        String response1, response2, response3, token;
        StringTokenizer tokenizer1;
        StringBuffer hostName, shareName;
        StringBuffer nmap, nmblookup, smbClient;
        ArrayList<String[]> networkInterfaces;
        ArrayList<Share> shareInfo;
        ArrayList<Node> nodes;
        Env env;
        isAnalyzed = false;
        tokenizer1 = null;
        token = null;
        networkInterface = null;
        networkInterfaces = null;
        networkInterfaceCounter = -1;
        nmap = null;
        nodes = null;
        shareInfo = null;
        hostName = null;
        env = new ShellEnv();
        response1 = env.exec("ifconfig | grep \"inet \" | cut -c 7-1000", true);
        if (!response1.equals("")) {
            response1 = response1.replace("inet", "").replace(" ", ":").replace("\r\n", "").replace("\r", "").replace("\n", "");
            tokenizer1 = new StringTokenizer(response1, ":");
            networkInterfaces = new ArrayList<String[]>();
        }
        while (!isAnalyzed) {
            if (!tokenizer1.hasMoreTokens()) {
                isAnalyzed = true;
            } else {
                token = tokenizer1.nextToken();
            }
            if ((token.equals("addr") || token.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}") || isAnalyzed) && !isAnalyzed) {
                networkInterfaceCounter++;
                networkInterface = new String[3];
                if (token.equals("addr")) {
                    networkInterface[0] = tokenizer1.nextToken();
                } else if (token.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
                    networkInterface[0] = token;
                }
                token = tokenizer1.nextToken();
                if (token.equals("Bcast") || token.equals("Mask")) {
                    if (token.equals("Bcast")) {
                        networkInterface[2] = tokenizer1.nextToken();
                        tokenizer1.nextToken();
                    }
                    networkInterface[1] = tokenizer1.nextToken();
                } else if (token.equals("netmask")) {
                    networkInterface[2] = convertSubnetMaskHexInDecimal(tokenizer1.nextToken());
                    if (!networkInterface[0].equals(LOCALHOSTADDRESS)) {
                        tokenizer1.nextToken();
                        networkInterface[1] = tokenizer1.nextToken();
                    }
                }
                networkInterfaces.add(networkInterfaceCounter, networkInterface);
            }
        }
        nodes = new ArrayList<Node>();
        for (int i = 0; i < networkInterfaces.size(); i++) {
            networkAddress = getNetworkAddress(networkInterfaces.get(i)[0], networkInterfaces.get(i)[1]);
            if (networkAddress != null && !networkAddress[0].equals(LOCALNETWORKADDRESS)) {
                nmap = new StringBuffer();
                nmap.append("nmap -T4 ");
                nmap.append(networkAddress[0] + "/" + networkAddress[1]);
                nmap.append(" | grep -E \"Interesting ports on | 139/tcp open | 445/tcp open\"");
                nmap.append(" | sed 's/Interesting ports on //'");
                nmap.append(" | cut -f2 -d\"(\" | cut -f1 -d \":\" | cut -f1 -d \")\"");
                System.out.println(nmap.toString());
                response1 = env.exec(nmap.toString(), true);
                if (response1 != null) {
                    tokenizer1 = new StringTokenizer(response1, "\n");
                    while (tokenizer1.hasMoreTokens()) {
                        token = ((String) tokenizer1.nextToken()).replaceAll("\n", "").replaceAll("\r", "").trim();
                        if (!token.equals("")) {
                            nmblookup = new StringBuffer("nmblookup -A " + token + " | grep \"<ACTIVE>\"");
                            System.out.println(nmblookup);
                            shareInfo = new ArrayList<Share>();
                            response2 = env.exec(nmblookup.toString(), true);
                            if (!response2.equals("")) {
                                hostName = new StringBuffer();
                                index = 0;
                                while (true) {
                                    if ((response2.charAt(index) >= 48 && response2.charAt(index) <= 122) && response2.charAt(index) != ' ') {
                                        hostName.append(response2.charAt(index));
                                    } else if (response2.charAt(index) == ' ' && response2.charAt(index - 1) >= 48 && response2.charAt(index - 1) <= 122) {
                                        break;
                                    }
                                    index++;
                                }
                                if (!hostName.equals("")) {
                                    Node node = new Node(hostName.toString(), token);
                                    node.setUsername("");
                                    node.setPassword("");
                                    smbClient = new StringBuffer("smbclient -L " + hostName.toString() + " -I " + token + " -N | grep \"Disk\"");
                                    System.out.println(smbClient);
                                    response3 = env.exec(smbClient.toString(), true);
                                    index = 0;
                                    shareName = null;
                                    isShareColumn = false;
                                    while (index < response3.length()) {
                                        if (index > 0 && (Character.isLetterOrDigit(response3.charAt(index)) || response3.charAt(index) == 36) && isShareColumn) {
                                            if (shareName == null) {
                                                shareName = new StringBuffer();
                                            }
                                            shareName.append(response3.charAt(index));
                                        } else if (response3.charAt(index) == 10 || index == 0) {
                                            isShareColumn = true;
                                        } else if (response3.charAt(index) == ' ' && isShareColumn) {
                                            if (!shareName.toString().contains("$")) {
                                                Share share = new Share();
                                                share.setName(shareName.toString());
                                                shareInfo.add(share);
                                            }
                                            shareName = null;
                                            isShareColumn = false;
                                        }
                                        index++;
                                    }
                                    node.setShares(shareInfo);
                                    nodes.add(node);
                                }
                            }
                        }
                    }
                }
            }
        }
        return nodes;
    }

    private String[] getNetworkAddress(String infAddress, String subnetMask) {
        boolean isCalculated;
        int counter, hostPortion, hostPerSubnet;
        int bitsAdded, numberOfSubnet, hostInNetwork;
        int[] arrSubnetMask, arrInterfaceAddress, arrNetworkAddress;
        StringTokenizer tokenizerSubnetMask, tokenizerInterfaceAddress;
        StringBuffer sbNetworkAddress;
        String token;
        HashMap<Integer, Integer> mapPortion;
        NetworkClass networkClass;
        ByteBuffer byteBuffer;
        if ((subnetMask == null || subnetMask.equals("")) || (infAddress == null || infAddress.equals(""))) {
            return null;
        }
        networkClass = null;
        tokenizerSubnetMask = new StringTokenizer(subnetMask, ".");
        tokenizerInterfaceAddress = new StringTokenizer(infAddress, ".");
        mapPortion = new HashMap<Integer, Integer>();
        arrSubnetMask = null;
        arrInterfaceAddress = null;
        arrNetworkAddress = null;
        hostPortion = 0;
        isCalculated = false;
        mapPortion.put(0, 8);
        mapPortion.put(128, 7);
        mapPortion.put(192, 6);
        mapPortion.put(224, 5);
        mapPortion.put(240, 4);
        mapPortion.put(248, 3);
        mapPortion.put(252, 2);
        mapPortion.put(254, 1);
        counter = 0;
        while (tokenizerSubnetMask.hasMoreTokens()) {
            if (arrSubnetMask == null) {
                arrSubnetMask = new int[4];
            }
            token = tokenizerSubnetMask.nextToken().trim();
            arrSubnetMask[counter] = Integer.parseInt(token);
            counter++;
        }
        counter = 0;
        while (tokenizerInterfaceAddress.hasMoreTokens()) {
            if (arrInterfaceAddress == null) {
                arrInterfaceAddress = new int[4];
            }
            arrInterfaceAddress[counter] = Integer.parseInt(tokenizerInterfaceAddress.nextToken());
            counter++;
        }
        if (arrInterfaceAddress[0] <= 127) {
            networkClass = NetworkClass.A;
        } else if (arrInterfaceAddress[0] >= 128 && arrInterfaceAddress[0] <= 191) {
            networkClass = NetworkClass.B;
        } else {
            networkClass = NetworkClass.C;
        }
        for (int i = 0; i < arrSubnetMask.length; i++) {
            if (arrSubnetMask[i] != 255) {
                hostPortion += mapPortion.get(arrSubnetMask[i]);
            }
        }
        hostPerSubnet = (int) Math.pow(2, hostPortion);
        if (arrNetworkAddress == null) {
            arrNetworkAddress = new int[4];
        }
        for (int i = 0; i < arrSubnetMask.length && !isCalculated; i++) {
            if (arrSubnetMask[i] == 255) {
                arrNetworkAddress[i] = arrInterfaceAddress[i];
            } else if (arrSubnetMask[i] == 0) {
                arrNetworkAddress[i] = 0;
            } else {
                byteBuffer = ByteBuffer.allocate(4);
                bitsAdded = 0;
                for (int j = arrInterfaceAddress.length - 1; j >= 0; j--) {
                    if (bitsAdded < networkClass.getHostAddressPortion()) {
                        byteBuffer.put((byte) arrInterfaceAddress[j]);
                    } else {
                        byteBuffer.put((byte) 0);
                    }
                    bitsAdded += 8;
                }
                byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
                hostInNetwork = byteBuffer.getInt(0);
                numberOfSubnet = hostInNetwork / hostPerSubnet;
                byteBuffer.clear();
                byteBuffer.putInt(numberOfSubnet * hostPerSubnet);
                byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
                bitsAdded = 0;
                for (int j = arrNetworkAddress.length - 1; bitsAdded < networkClass.getHostAddressPortion(); j--) {
                    arrNetworkAddress[j] = byteBuffer.get(bitsAdded / 8) & 255;
                    bitsAdded += 8;
                }
                isCalculated = true;
            }
        }
        sbNetworkAddress = new StringBuffer();
        for (int i = 0; i < arrNetworkAddress.length; i++) {
            if (i > 0) {
                sbNetworkAddress.append(".");
            }
            sbNetworkAddress.append(arrNetworkAddress[i]);
        }
        return new String[] { sbNetworkAddress.toString(), (32 - hostPortion) + "" };
    }

    public String convertSubnetMaskHexInDecimal(String hex) {
        String subnetMask;
        String[] subnetMaskArr = new String[4];
        if (hex != null && !hex.equals("") && hex.startsWith("0x") && hex.length() == 10) {
            subnetMaskArr[0] = hex.substring(2, 4);
            subnetMaskArr[1] = hex.substring(4, 6);
            subnetMaskArr[2] = hex.substring(6, 8);
            subnetMaskArr[3] = hex.substring(8, 10);
        }
        try {
            subnetMask = Integer.decode("0x" + subnetMaskArr[0]) + "." + Integer.decode("0x" + subnetMaskArr[1]) + "." + Integer.decode("0x" + subnetMaskArr[2]) + "." + Integer.decode("0x" + subnetMaskArr[3]);
        } catch (Exception ex) {
            subnetMask = null;
        }
        return subnetMask;
    }
}
