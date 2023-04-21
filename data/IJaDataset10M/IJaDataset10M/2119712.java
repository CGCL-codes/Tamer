package nhb.webflag.importtools.source.armorycharacter;

import java.io.IOException;
import java.io.InputStream;
import nhb.webflag.importtools.parser.xml.XMLInput;
import nhb.webflag.importtools.parser.xml.XMLOutput;
import nhb.webflag.util.HttpClient;
import nhb.webflag.util.StringUtils;
import org.apache.log4j.Logger;
import org.jdom.Element;

/**
 * reads a character from the armory
 *
 * @author hendrik
 */
public class CharacterReader {

    private static Logger logger = Logger.getLogger(CharacterReader.class);

    public ArmoryCharacter getCharacter(String realm, String characterName) {
        try {
            StringBuilder url = new StringBuilder();
            url.append("http://armory.wow-europe.com/character-sheet.xml?locale=en_us&r=");
            url.append(StringUtils.encodeURL(realm));
            url.append("&n=");
            url.append(StringUtils.encodeURL(characterName));
            HttpClient client = new HttpClient(url.toString());
            InputStream is = client.getInputStream();
            if (is == null) {
                logger.error("Input stream is null");
                return null;
            }
            Element root = XMLInput.read(is);
            is.close();
            return new ArmoryCharacter(root);
        } catch (IOException e) {
            logger.warn(e, e);
            return null;
        }
    }

    public static void main(String[] args) throws IOException {
        HttpClient client = new HttpClient("http://armory.wow-europe.com/character-sheet.xml?locale=en_us&r=Der+abyssische+Rat&n=Abaka");
        InputStream is = client.getInputStream();
        Element root = XMLInput.read(is);
        is.close();
        XMLOutput.write(root, System.out);
        ArmoryCharacter character = new ArmoryCharacter(root);
        System.out.println(character.getData());
    }
}
