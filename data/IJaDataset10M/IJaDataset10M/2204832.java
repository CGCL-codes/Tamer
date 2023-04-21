package net.cmp4oaw.ea.uml2writer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Vector;
import net.cmp4oaw.common.DefaultPackageFilter;
import net.cmp4oaw.ea.uml2writer.test.oaw_testbase;
import net.cmp4oaw.ea_com.exception.EA_PathNotFoundException;
import net.cmp4oaw.ea_com.repository.EA_Package;
import net.cmp4oaw.ea_com.repository.EA_Repository;
import net.cmp4oaw.uml2.support.EA_ProfileDef;
import net.cmp4oaw.uml2export.mwe.EA_XmiWriter;
import org.eclipse.emf.common.util.URI;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Profile;

/**
 *
 * @author MarkusK
 */
public class ModelLoader {

    private static final String EAP_FILE = "src/test/resources/EA_Toolsupport_Test.eap";

    private static final String MODEL_FILE = "Output/TestModel.uml";

    private static final String PROFILE_FILE = "Output/ea_test.profile.uml";

    private static final String PROFILE_NAME = "ea_test";

    private static final String MODEL_PKG = "Views/Test View";

    private static final String PROFILE_PKG = "Views/Profile View/ea_test";

    private static final String EA_PROFILE_FILE = "src/main/resources/ea.profile.uml";

    private static final String GHOSTPACKAGE_NAME = "GhostElements";

    private static Model model;

    private static Profile profile;

    private static EA_Repository rep = EA_Repository.getInstance();

    private static EA_XmiWriter xmiWriter = new EA_XmiWriter();

    public static Model getModel() throws FileNotFoundException, EA_PathNotFoundException, EA_ProfileNotFound {
        if (model == null) setupTest();
        return model;
    }

    public static Profile getProfile() throws FileNotFoundException, EA_PathNotFoundException, EA_ProfileNotFound {
        if (profile == null) setupTest();
        return profile;
    }

    private static void setupTest() throws FileNotFoundException, EA_PathNotFoundException, EA_ProfileNotFound {
        System.out.println("UnitTest started....");
        rep.openModel(new File(EAP_FILE).getAbsolutePath());
        exportProfile();
        exportModel();
        rep.closeModel();
    }

    private static void exportModel() throws EA_PathNotFoundException, EA_ProfileNotFound {
        System.out.println("Model Export: Start...");
        rep.reset();
        EA_UML2Writer writer = new EA_UML2Writer();
        writer.setParseProfile(false);
        writer.setDebug(true);
        writer.setIssues(new oaw_testbase());
        writer.setPackageFilter(new DefaultPackageFilter());
        writer.setFileName(new File(MODEL_FILE).getAbsolutePath());
        writer.setLoadJavaTypes(true);
        writer.setGhostPackageName(GHOSTPACKAGE_NAME);
        writer.setXmiWriter(xmiWriter);
        Vector<String> list = new Vector<String>();
        list.add(PROFILE_FILE);
        list.add(EA_PROFILE_FILE);
        writer.setProfile(list);
        Vector<EA_ProfileDef> pDefList = new Vector<EA_ProfileDef>();
        pDefList.add(EA_ProfileDef.create("UnitTest", PROFILE_NAME));
        writer.setPkgProfiles(pDefList);
        EA_Package p = rep.findPackageByPath(MODEL_PKG);
        writer.setPkgToExport(p);
        writer.setAllSubPackages(true);
        rep.accept(writer);
        writer.saveFile();
        URI uri = URI.createFileURI(MODEL_FILE);
        model = (Model) xmiWriter.load(uri);
        System.out.println("Model Export: End.");
    }

    private static void exportProfile() throws EA_PathNotFoundException {
        System.out.println("Profile Export: Start...");
        rep.reset();
        EA_UML2Writer writer = EA_UML2ProfileWriter.getInstance();
        writer.setParseProfile(true);
        writer.setDebug(true);
        writer.setIssues(new oaw_testbase());
        writer.setPackageFilter(new DefaultPackageFilter());
        writer.setFileName(new File(PROFILE_FILE).getAbsolutePath());
        writer.setXmiWriter(new EA_XmiWriter());
        EA_Package p = rep.findPackageByPath(PROFILE_PKG);
        writer.setPkgToExport(p);
        rep.accept(writer);
        writer.saveFile();
        URI uri = URI.createFileURI(PROFILE_FILE);
        profile = (Profile) xmiWriter.load(uri);
        System.out.println("Profile Export: End.");
    }
}
