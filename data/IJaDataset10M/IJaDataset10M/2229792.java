package boccaccio.andrea.settings;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

public class ConcreteSettingsFromPropsXmlFile extends AbsSettingsFromProps {

    private static ConcreteSettingsFromPropsXmlFile instance = null;

    private ConcreteSettingsFromPropsXmlFile() {
        this.setLoaded(false);
        this.setProp(new Properties());
        this.setType("propsXml");
    }

    protected static ConcreteSettingsFromPropsXmlFile getInstance() {
        if (instance == null) instance = new ConcreteSettingsFromPropsXmlFile();
        return instance;
    }

    @Override
    protected void load(String strFileName) {
        try {
            this.getProp().loadFromXML(new FileInputStream(strFileName));
            this.toAppSettings();
            this.setLoaded(true);
        } catch (InvalidPropertiesFormatException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
