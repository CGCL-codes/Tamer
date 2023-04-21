package domain;

import java.util.ArrayList;
import misc.Env;
import misc.ShellEnv;

public class Node {

    private int m_Id;

    private String m_Name;

    private String m_IpAddress;

    private String m_Username;

    private String m_Password;

    private ArrayList<Share> m_Shares;

    public Node(String name, String ipAddress) {
        m_Name = name;
        m_IpAddress = ipAddress;
    }

    public Node(int id, String name, String ipAddress, ArrayList<Share> shares) {
        m_Id = id;
        m_IpAddress = ipAddress;
        m_Name = name;
        m_Shares = shares;
    }

    public void addShare(String shareName) {
        if (m_Shares == null) {
            m_Shares = new ArrayList<Share>();
        }
        m_Shares.add(new Share(shareName.toUpperCase()));
    }

    public void addShare(Share share) {
        if (m_Shares == null) {
            m_Shares = new ArrayList<Share>();
        }
        m_Shares.add(share);
    }

    public boolean removeShare(Share share) {
        return m_Shares.remove(share);
    }

    public boolean removeShare(String shareName) {
        Share tempShare;
        tempShare = null;
        for (Share share : m_Shares) {
            if (share.getName().equals(shareName)) {
                tempShare = share;
                break;
            }
        }
        if (tempShare != null) {
            m_Shares.remove(tempShare);
            return true;
        }
        return false;
    }

    public void removeAllShares(String mountPoint) {
        for (Share share : m_Shares) {
            share.unmountShare(mountPoint, m_Name);
            share.removeMountPoint(mountPoint, m_Name);
        }
        m_Shares.clear();
    }

    public int getNumberOfShares() {
        if (m_Shares != null) {
            return m_Shares.size();
        }
        return 0;
    }

    public String getName() {
        return m_Name;
    }

    public String getIpAddress() {
        return m_IpAddress;
    }

    public String getUsername() {
        return m_Username;
    }

    public void setUsername(String username) {
        m_Username = username;
    }

    public String getPassword() {
        return m_Password;
    }

    public void setPassword(String password) {
        m_Password = password;
    }

    public int getId() {
        return m_Id;
    }

    public ArrayList<Share> getShares() {
        return m_Shares;
    }

    public void setShares(ArrayList<Share> shares) {
        m_Shares = shares;
    }

    public void setName(String name) {
        m_Name = name;
    }

    public void setIpAddress(String ipAddress) {
        m_IpAddress = ipAddress;
    }

    public ArrayList<Share> searchShare() {
        String response;
        Env env = new ShellEnv();
        int index;
        StringBuffer shareName;
        boolean isShareColumn;
        ArrayList<Share> shares;
        Share share;
        response = env.exec("smbclient -L " + m_Name + " -I " + m_IpAddress + " -U " + m_Username + "%" + m_Password + " | grep \"Disk\"", true);
        System.out.println(response);
        index = 0;
        shareName = null;
        isShareColumn = false;
        shares = new ArrayList<Share>();
        while (index < response.length()) {
            if (index > 0 && (Character.isLetterOrDigit(response.charAt(index)) || response.charAt(index) == 36) && isShareColumn) {
                if (shareName == null) {
                    shareName = new StringBuffer();
                }
                shareName.append(response.charAt(index));
            } else if (response.charAt(index) == 10 || index == 0) {
                isShareColumn = true;
            } else if (response.charAt(index) == ' ' && isShareColumn) {
                if (!shareName.toString().contains("$")) {
                    share = new Share();
                    share.setName(shareName.toString());
                    shares.add(share);
                }
                shareName = null;
                isShareColumn = false;
            }
            index++;
        }
        return shares;
    }
}
