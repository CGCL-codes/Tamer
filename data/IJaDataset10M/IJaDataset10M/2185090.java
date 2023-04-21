package net.sf.fc.script;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import net.sf.fc.script.gen.copy.CopyScript;
import net.sf.fc.script.gen.copy.Dst;
import net.sf.fc.script.gen.copy.Map;
import net.sf.fc.script.gen.copy.Src;
import net.sf.fc.script.gen.copy.Type;
import net.sf.fc.script.gen.options.OptionsScript;
import net.sf.fc.script.gen.options.RenameOptions;
import net.sf.fc.script.gen.options.RenameSuffixOptions;
import net.sf.fc.script.gen.options.RenameType;
import net.sf.fc.script.gen.options.SrcDirOptions;
import net.sf.fc.script.gen.settings.FileList;
import net.sf.fc.script.gen.settings.LogLevel;
import net.sf.fc.script.gen.settings.LookAndFeel;
import net.sf.fc.script.gen.settings.MostRecentlyUsed;
import net.sf.fc.script.gen.settings.Paths;
import net.sf.fc.script.gen.settings.Settings;

import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;

import static net.sf.fc.script.TestUtil.getTestDirectory;

public class ScriptHelperTest {

    @Test
    public void testMarshalOptions() throws JAXBException, SAXException, IOException {

        // Create an OptionsScript object with all default values.
        OptionsScript options = createDefaultOptions();

        try {
            ScriptHelper<OptionsScript> helper = ScriptHelper.createScriptHelper(OptionsScript.class, SchemaData.OPTIONS);

            File optionsXml = new File(getTestDirectory(), "options.xml");

            helper.marshal(optionsXml, options);

            assertTrue(optionsXml.exists());
        } catch(SAXException e) {
            e.printStackTrace();
            assertTrue(false);
        } catch(JAXBException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testValidateOptions() {
        try {
            ScriptHelper<OptionsScript> helper = ScriptHelper.createScriptHelper(OptionsScript.class, SchemaData.OPTIONS);

            File optionsXml = new File(getTestDirectory(), "options.xml");

            helper.validate(optionsXml);

            assertTrue(true);

        } catch(SAXException e) {
            e.printStackTrace();
            assertTrue(false);
        } catch(JAXBException e) {
            e.printStackTrace();
            assertTrue(false);
        } catch(IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testUnmarshalOptions() {

        try {
            ScriptHelper<OptionsScript> helper = ScriptHelper.createScriptHelper(OptionsScript.class, SchemaData.OPTIONS);

            File optionsXml = new File(getTestDirectory(), "options.xml");

            OptionsScript o = helper.unmarshal(optionsXml);

            // Assert that all values are set to their defaults.
            assertEquals(RenameType.RENAME_AUTO, o.getRenameOptions().getRenameType());
            assertEquals("_%tY%tm%td_%tH%tM%tS%tL", o.getRenameOptions().getRenameSuffixOptions().getFormat());
            assertEquals(false, o.getRenameOptions().getRenameSuffixOptions().isUseGMT());

            assertEquals(null, o.getSrcDirOptions().getFileCopyFilter());
            assertEquals(null, o.getSrcDirOptions().getDirCopyFilter());
            assertEquals(null, o.getSrcDirOptions().getFlattenFilter());
            assertEquals(null, o.getSrcDirOptions().getMergeFilter());
            assertEquals(0, o.getSrcDirOptions().getMaxCopyLevel());
            assertEquals(-1, o.getSrcDirOptions().getMaxFlattenLevel());
            assertEquals(-1, o.getSrcDirOptions().getMaxMergeLevel());

            assertEquals(true, o.isCopyAttributes());

        } catch(SAXException e) {
            e.printStackTrace();
            assertTrue(false);
        } catch(JAXBException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testMarshalSettings() throws JAXBException, SAXException, IOException {

        // Create a Settings object with all default values.
        MostRecentlyUsed mru = new MostRecentlyUsed();
        mru.setFileList(new FileList());
        LookAndFeel laf = new LookAndFeel();
        Paths paths = new Paths();
        paths.setCopyPath("");
        paths.setOptionsPath("");
        paths.setRestorePath("");
        Settings settings = new Settings();
        settings.setMostRecentlyUsed(mru);
        settings.setLookAndFeel(laf);
        settings.setPaths(paths);

        try {
            ScriptHelper<Settings> helper = ScriptHelper.createScriptHelper(Settings.class, SchemaData.SETTINGS);

            File settingsXml = new File(getTestDirectory(), "settings.xml");

            helper.marshal(settingsXml, settings);

            assertTrue(settingsXml.exists());
        } catch(SAXException e) {
            e.printStackTrace();
            assertTrue(false);
        } catch(JAXBException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testValidateSettings() {
        try {
            ScriptHelper<Settings> helper = ScriptHelper.createScriptHelper(Settings.class, SchemaData.SETTINGS);

            File settingsXml = new File(getTestDirectory(), "settings.xml");

            helper.validate(settingsXml);

            assertTrue(true);

        } catch(SAXException e) {
            e.printStackTrace();
            assertTrue(false);
        } catch(JAXBException e) {
            e.printStackTrace();
            assertTrue(false);
        } catch(IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testUnmarshalSettings() {

        try {
            ScriptHelper<Settings> helper = ScriptHelper.createScriptHelper(Settings.class, SchemaData.SETTINGS);

            File settingsXml = new File(getTestDirectory(), "settings.xml");

            Settings s = helper.unmarshal(settingsXml);

            // Assert that all values are set to their defaults.
            assertEquals(LogLevel.INFO, s.getLogLevel());
            assertEquals(new LookAndFeel(), s.getLookAndFeel());
            MostRecentlyUsed mru = new MostRecentlyUsed();
            mru.setFileList(new FileList());
            assertEquals(mru, s.getMostRecentlyUsed());
            Paths paths = new Paths();
            paths.setCopyPath("");
            paths.setOptionsPath("");
            paths.setRestorePath("");
            assertEquals(paths, s.getPaths());

        } catch(SAXException e) {
            e.printStackTrace();
            assertTrue(false);
        } catch(JAXBException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testMarshalCopyScript() throws JAXBException, SAXException, IOException {

        // Create a CopyScript object with all default values.
        CopyScript copyScript = new CopyScript();
        copyScript.setDefaultOptions(createDefaultOptions());
        copyScript.getMapList().add(createDefaultMap());

        try {
            ScriptHelper<CopyScript> helper = ScriptHelper.createScriptHelper(CopyScript.class, SchemaData.COPY);

            File copyScriptXml = new File(getTestDirectory(), "copyScript.xml");

            helper.marshal(copyScriptXml, copyScript);

            assertTrue(copyScriptXml.exists());
        } catch(SAXException e) {
            e.printStackTrace();
            assertTrue(false);
        } catch(JAXBException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testValidateCopyScript() {
        try {
            ScriptHelper<CopyScript> helper = ScriptHelper.createScriptHelper(CopyScript.class, SchemaData.COPY);

            File copyScriptXml = new File(getTestDirectory(), "copyScript.xml");

            helper.validate(copyScriptXml);

            assertTrue(true);

        } catch(SAXException e) {
            e.printStackTrace();
            assertTrue(false);
        } catch(JAXBException e) {
            e.printStackTrace();
            assertTrue(false);
        } catch(IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testUnmarshalCopyScript() {

        try {
            ScriptHelper<CopyScript> helper = ScriptHelper.createScriptHelper(CopyScript.class, SchemaData.COPY);

            File copyScriptXml = new File(getTestDirectory(), "copyScript.xml");

            CopyScript cs = helper.unmarshal(copyScriptXml);

            // Assert that all values are set to their defaults.
            assertEquals(createDefaultOptions(), cs.getDefaultOptions());
            List<Map> mapList = new ArrayList<>();
            mapList.add(createDefaultMap());
            assertEquals(mapList, cs.getMapList());

        } catch(SAXException e) {
            e.printStackTrace();
            assertTrue(false);
        } catch(JAXBException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Ignore
    public OptionsScript createDefaultOptions() {
        RenameOptions ro = new RenameOptions();
        ro.setRenameSuffixOptions(new RenameSuffixOptions());

        SrcDirOptions sdo = new SrcDirOptions();

        OptionsScript options = new OptionsScript();
        options.setRenameOptions(ro);
        options.setSrcDirOptions(sdo);

        return options;
    }

    @Ignore
    Map createDefaultMap() {
        Map map = new Map();
        map.setSrc(createDefaultSrc());
        map.getDstList().add(createDefaultDst());
        return map;
    }

    @Ignore
    Src createDefaultSrc() {
        Src src = new Src();
        src.setPath(new File(System.getProperty("user.dir")));
        return src;
    }

    @Ignore
    Dst createDefaultDst() {
        Dst dst = new Dst();
        dst.setType(Type.DIRECTORY);
        dst.getPathList().add(new File(System.getProperty("user.dir")));
        return dst;
    }
}
