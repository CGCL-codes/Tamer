package egs.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AuthenticationManager {

    private Map<String, String> userPassMap;

    public AuthenticationManager() {
        userPassMap = new HashMap<String, String>();
    }

    public AuthenticationManager(String filePath) {
        userPassMap = new HashMap<String, String>();
        parseFile(filePath);
    }

    public void parseFile(String filePath) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(new File(filePath)));
        } catch (FileNotFoundException e) {
            System.err.println("Error opening password file.");
            e.printStackTrace();
        }
        if (reader != null) {
            try {
                String line = reader.readLine();
                while (line != null) {
                    String[] fields = line.split("\t");
                    String username = fields[0];
                    String password = fields[1];
                    addUser(username, password);
                    line = reader.readLine();
                }
            } catch (IOException e) {
                System.err.println("Error reading password file.");
                e.printStackTrace();
            }
        }
    }

    public boolean addUser(String username, String password) {
        if (userPassMap.containsKey(username)) return false;
        userPassMap.put(username, password);
        return true;
    }

    public boolean isAuthenticated(String username, String password) {
        if (!userPassMap.containsKey(username)) return false;
        return userPassMap.get(username).equals(password);
    }

    public static void main(String argv[]) throws IOException {
        String filePath = "/home/mburkhol/svn/cs544-checkers/trunk/passwd";
        AuthenticationManager authMgr = new AuthenticationManager(filePath);
        System.out.println(authMgr.isAuthenticated("matt", "mattpw"));
        System.out.println(authMgr.isAuthenticated("matt", "matt"));
        System.out.println(authMgr.isAuthenticated("mat", "mattpw"));
    }
}
