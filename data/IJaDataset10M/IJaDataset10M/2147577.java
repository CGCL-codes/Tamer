package com.informatics.polymer.server.browserepository;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import nu.xom.Document;
import nu.xom.Element;
import org.apache.commons.io.FileUtils;
import org.openscience.cdk.exception.CDKException;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.ResourceException;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;
import com.informatics.polymer.server.database.DatabaseInteraction;

/**
 * Restlet to extract all repeat units in the derby database for viewing.
 * 
 * @author ed
 * @version 1.0
 */
public class ViewRepository extends Resource {

    public void init(Context context, Request request, Response response) {
        super.init(context, request, response);
        getVariants().add(new Variant(MediaType.TEXT_PLAIN));
    }

    public Representation represent(Variant variant) throws ResourceException {
        Representation resource = null;
        if (!MediaType.TEXT_PLAIN.includes(variant.getMediaType())) {
            throw new ResourceException(Status.CLIENT_ERROR_NOT_ACCEPTABLE);
        } else {
            StringBuffer sb = new StringBuffer();
            DatabaseInteraction databaseInteraction = new DatabaseInteraction();
            try {
                databaseInteraction.init();
                List<com.informatics.polymer.server.database.RepeatUnit> resu = databaseInteraction.getAllRepeatUnits();
                Element root = new Element("RepeatUnits");
                for (com.informatics.polymer.server.database.RepeatUnit ru : resu) {
                    double[] descriptors = ru.getDescriptors();
                    Element repeatUnit = new Element("RepeatUnit");
                    Element id = new Element("Id");
                    id.appendChild(ru.getId());
                    Element ruPictureURL = new Element("URL");
                    ruPictureURL.appendChild("http://localhost:8080/RepeatUnitPictures/" + ru.getId().trim() + ".png");
                    Element smilesString = new Element("Smiles");
                    smilesString.appendChild(ru.getSmilesString());
                    repeatUnit.appendChild(id);
                    repeatUnit.appendChild(ruPictureURL);
                    repeatUnit.appendChild(smilesString);
                    Element alogP = new Element("ALOGP");
                    alogP.appendChild(Double.toString(descriptors[0]));
                    Element alogp2 = new Element("AlogP2");
                    alogp2.appendChild(Double.toString(descriptors[1]));
                    Element amr = new Element("AMR");
                    amr.appendChild(Double.toString(descriptors[2]));
                    Element apol = new Element("APol");
                    apol.appendChild(Double.toString(descriptors[3]));
                    Element aromaticAtomCount = new Element("AromaticAtomCount");
                    aromaticAtomCount.appendChild(Double.toString(descriptors[4]));
                    Element aromaticBondCount = new Element("AromaticBondCount");
                    aromaticBondCount.appendChild(Double.toString(descriptors[5]));
                    Element atomCount = new Element("AtomCount");
                    atomCount.appendChild(Double.toString(descriptors[6]));
                    Element bondCount = new Element("BondCount");
                    bondCount.appendChild(Double.toString(descriptors[7]));
                    Element fragmentComplexity = new Element("FragmentComplexity");
                    fragmentComplexity.appendChild(Double.toString(descriptors[8]));
                    Element hbaCount = new Element("HBACount");
                    hbaCount.appendChild(Double.toString(descriptors[9]));
                    Element hbdCount = new Element("HBDCount");
                    hbdCount.appendChild(Double.toString(descriptors[10]));
                    Element kappa1 = new Element("Kappa1");
                    kappa1.appendChild(Double.toString(descriptors[11]));
                    Element kappa2 = new Element("Kappa2");
                    kappa2.appendChild(Double.toString(descriptors[12]));
                    Element kappa3 = new Element("Kappa3");
                    kappa3.appendChild(Double.toString(descriptors[13]));
                    Element ruleOf5 = new Element("LipinskiRuleOf5Failures");
                    ruleOf5.appendChild(Double.toString(descriptors[14]));
                    Element tpsa = new Element("TPSA");
                    tpsa.appendChild(Double.toString(descriptors[15]));
                    repeatUnit.appendChild(alogP);
                    repeatUnit.appendChild(alogp2);
                    repeatUnit.appendChild(amr);
                    repeatUnit.appendChild(apol);
                    repeatUnit.appendChild(aromaticAtomCount);
                    repeatUnit.appendChild(aromaticBondCount);
                    repeatUnit.appendChild(atomCount);
                    repeatUnit.appendChild(bondCount);
                    repeatUnit.appendChild(fragmentComplexity);
                    repeatUnit.appendChild(hbaCount);
                    repeatUnit.appendChild(hbdCount);
                    repeatUnit.appendChild(kappa1);
                    repeatUnit.appendChild(kappa2);
                    repeatUnit.appendChild(kappa3);
                    repeatUnit.appendChild(ruleOf5);
                    repeatUnit.appendChild(tpsa);
                    root.appendChild(repeatUnit);
                    sb.append("\n");
                }
                Document doc = new Document(root);
                String f = "/DataBaseDump" + ".xml";
                String curDir = new String(System.getProperty("user.dir"));
                File databaseXml = new File(curDir + f);
                try {
                    FileUtils.writeStringToFile(databaseXml, doc.toXML());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            resource = new StringRepresentation(sb.toString(), MediaType.TEXT_PLAIN);
            return resource;
        }
    }
}
