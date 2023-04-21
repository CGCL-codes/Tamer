package jadacommon.operation;

/**
 *
 * @author FILIPPO
 */
public class Translation {

    static Translation instance = new Translation();

    private String lang;

    private ReadXml in;

    /**
     * The constructor associates the languages with their files XML for the translation[examples: Italian-> italian.xml; Italiano-> english.xml]
     * It's private and it's only be called by getInstance() method because the Object Translation is type Singleton
     */
    private Translation() {
        this.lang = null;
    }

    /**
     * If the instance_flag is false,this method creates an instance of the Object Translation(Singleton Pattern)
     * @return A new Object Translation if instance_flag is false, else it returns a null Object Translation
     */
    public static Translation getInstance() {
        return instance;
    }

    /**
     * Set the language of translation
     * @param the language chosen from the array of strings returned by the method getListOfLang()
     */
    public void setLang(String language) {
        this.lang = language;
        String curDir = System.getProperty("user.dir");
        in = new ReadXml(curDir + "/language/" + lang);
    }

    /**
     * Get the current language
     * @return The current language
     */
    public String getLang() {
        return lang;
    }

    /**
     * Translates the name of a component in the chosen language 
     * @param nameComponent The name of the component to translate[example: JButton button1, translateCompenent("button1"]
     * @return Translated label for the component
     */
    public String translateComponent(String nameComponent) {
        if (in.GetDataFromTagByAttr("translation", "id", nameComponent).isEmpty()) return nameComponent; else return in.GetDataFromTagByAttr("translation", "id", nameComponent).toString();
    }
}
