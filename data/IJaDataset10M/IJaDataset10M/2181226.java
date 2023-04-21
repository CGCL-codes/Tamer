package net.emotivecloud.vrmm.vtm.resourcefabrics.repositoryimage.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import net.emotivecloud.vrmm.vtm.repositoryimage.client.Domain;
import org.apache.xmlbeans.XmlException;
import edu.upc.ac.www.rfl.HardwareDescriptionDocument;
import edu.upc.ac.www.rfl.HardwareDescriptionType;
import edu.upc.ac.www.rfl.ImageDescriptionDocument;
import edu.upc.ac.www.rfl.MachineDescriptionDocument;
import edu.upc.ac.www.rfl.MachineDescriptionType;

public class Tester {

    public static void main(String[] args) {
        System.out.println("[greig] Tester start");
        Domain domain = new Domain();
        domain.setIp("192.168.1.83");
        File file, file2 = null;
        FileReader fr, fr2 = null;
        BufferedReader br, br2 = null;
        String line = " ", hardware = " ", software = " ";
        try {
            file2 = new File("/home/greig/workspace/softwareDescription");
            fr2 = new FileReader(file2);
            br2 = new BufferedReader(fr2);
            while ((line = br2.readLine()) != null) software = software + line;
            System.out.println("software " + software);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ImageDescriptionDocument idd;
        HardwareDescriptionDocument hdd;
        try {
            idd = ImageDescriptionDocument.Factory.parse(software);
            hdd = HardwareDescriptionDocument.Factory.newInstance();
            HardwareDescriptionType hdt = hdd.getHardwareDescription();
            MachineDescriptionDocument mdd = MachineDescriptionDocument.Factory.newInstance();
            MachineDescriptionType mdt = mdd.addNewMachineDescription();
            mdt.setHardwareDescription(hdd.getHardwareDescription());
            mdt.setImageDescription(idd.getImageDescription());
            String name = "maquinaTest" + System.currentTimeMillis();
            mdt.setName(name);
            domain = MachineDescriptionToDomain.convert(mdd);
        } catch (XmlException e1) {
            e1.printStackTrace();
        }
        System.out.println("[greig] Tester: before create " + domain);
        RepositoryImageClient client = new RepositoryImageClient();
        System.out.println("[Tester] after create RIClient");
        try {
            client.create(domain, "172.20.0.11", 8080);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("[Tester] Machine created");
    }
}
