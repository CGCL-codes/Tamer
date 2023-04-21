package org.matsim.socialnetworks.io;

import java.io.BufferedReader;
import java.io.FileReader;
import org.apache.log4j.Logger;
import org.matsim.plans.Person;
import org.matsim.plans.Plans;
import org.matsim.socialnetworks.socialnet.SocialNetEdge;
import org.matsim.socialnetworks.socialnet.SocialNetwork;

public class MakeSocialNetworkFromFile {

    private Plans plans;

    private SocialNetwork snet;

    private final Logger log = Logger.getLogger(MakeSocialNetworkFromFile.class);

    public MakeSocialNetworkFromFile(SocialNetwork snet, Plans plans) {
        this.plans = plans;
        this.snet = snet;
    }

    public void read(String fileName, int iterToLoad) {
        BufferedReader br = null;
        try {
            FileReader fis = new FileReader(fileName);
            br = new BufferedReader(fis);
            String thisLineOfData = null;
            int i = 0;
            while ((thisLineOfData = br.readLine()) != null) {
                if (i > 0) {
                    String[] s;
                    String patternStr = " ";
                    s = thisLineOfData.split(patternStr);
                    int iter = Integer.valueOf(s[0]).intValue();
                    String egoId = s[4];
                    String alterId = s[5];
                    String purpose = s[6];
                    int timesmet = Integer.valueOf(s[7]).intValue();
                    if (iterToLoad == iter) {
                        Person person1 = plans.getPerson(egoId);
                        Person person2 = plans.getPerson(alterId);
                        snet.makeSocialContact(person1, person2, 0, purpose);
                        SocialNetEdge thisEdge = person1.getKnowledge().egoNet.getEgoLink(person2);
                        thisEdge.setNumberOfTimesMet(timesmet);
                    }
                }
                i++;
                if (i % 1000 == 0) {
                    log.info("   Edge " + i);
                }
            }
            br.close();
        } catch (Exception e) {
            System.err.println(" File input error: " + fileName);
        }
    }
}
