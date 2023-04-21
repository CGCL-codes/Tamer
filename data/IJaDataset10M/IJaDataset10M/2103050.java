package titan.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ServiceComposer {

    private List<Service> m_Services = new ArrayList<Service>();

    /** Initializes the service composer. Scans the application directory 
	 * and collects all service descriptions.
	 * @param strServerPath Application directory
	 */
    public ServiceComposer(String strServerPath) {
        File directory = new File(strServerPath);
        if (!directory.exists()) {
            System.err.println("Could not find directory: " + strServerPath);
            return;
        }
        String[] files = directory.list();
        for (int i = 0; i < files.length; i++) {
            if (files[i].endsWith(".srv")) {
                try {
                    System.out.println("Adding: " + strServerPath + files[i]);
                    File servFile = new File(strServerPath + files[i]);
                    FileInputStream rcStream = new FileInputStream(servFile);
                    byte[] data = new byte[(int) servFile.length()];
                    rcStream.read(data);
                    rcStream.close();
                    try {
                        Service newService;
                        newService = new Service(new String(data));
                        m_Services.add(newService);
                    } catch (Exception e) {
                        System.err.println("Illegal service format in for " + files[i]);
                    }
                } catch (IOException e) {
                    System.err.println("Could not find or read " + files[i]);
                }
            }
        }
    }

    /** Returns a list of services which are implementable with the given set of 
	 *  services.
	 */
    public String composeServices(int[] services) {
        String strAnswer = "";
        for (int i = 0; i < m_Services.size(); i++) {
            if (m_Services.get(i).isExecutable(services)) {
                strAnswer += m_Services.get(i) + "\n";
            }
        }
        return strAnswer;
    }

    public static void main(String[] args) {
        ServiceComposer sc = new ServiceComposer("titan/applications/");
        int[] services = { 1, 2, 3, 4, 5, 6, 7 };
        String strAnswer = sc.composeServices(services);
        System.out.println("Registered services:\n" + strAnswer);
    }
}
