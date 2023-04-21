package wfrpv2.gui;

import java.applet.Applet;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import misc.intsStrings;
import wfrpv2.dataTypes.Career;
import wfrpv2.dataTypes.Character;
import wfrpv2.helpers.GeneralFunctions;
import wfrpv2.helpers.IOFunctions;
import wfrpv2.helpers.get_attribute;
import wfrpv2.helpers.save;

public class guiHelpers {

    static JComponent characterFrontPanel;

    static JComponent characterBackPanel;

    /** Returns an ImageIcon, or null if the path was invalid. */
    public static ImageIcon createImageIcon(String Logo) {
        String path = System.getProperty("user.dir");
        path = path.concat("/dataFiles/Images/");
        path = path.concat(Logo);
        File newfile = new File(path);
        if (newfile.exists()) {
            return new ImageIcon(newfile.getPath());
        } else {
            System.err.println("Couldn't find file: " + newfile.getPath());
            return null;
        }
    }

    public static JComponent makeTextPanel(String text) {
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel(text);
        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(filler);
        return panel;
    }

    /**
	 * @param attrib
	 * @return integer position of the attribute in the profile.
	 */
    public static int lookupAttrib(String attrib) {
        Career career = new Career();
        for (int i = 0; i < 16; i++) {
            if (attrib.equals(career.profile[i])) {
                return i;
            }
        }
        return 0;
    }

    /**
	 * @param character
     * @throws FileNotFoundException
	 */
    static String update_sheet_back(Character character) throws IOException {
        String charactersheet = "";
        String characterSheetBack = System.getProperty("user.dir");
        characterSheetBack = characterSheetBack.concat("/dataFiles/html/back.html");
        try {
            FileInputStream fis = new FileInputStream(characterSheetBack);
            FileChannel fc = fis.getChannel();
            MappedByteBuffer mbf = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            byte[] barray = new byte[(int) (fc.size())];
            mbf.get(barray);
            charactersheet = new String(barray);
        } finally {
        }
        charactersheet = charactersheet.replaceAll("_SKILLS_", table(character.skills));
        charactersheet = charactersheet.replaceAll("_TALENTS_", table(character.talents));
        charactersheet = charactersheet.replaceAll("_TRAPPINGS_", table(character.trappings));
        return charactersheet;
    }

    /**
	 * @param skills
	 * @return
	 */
    private static String table(List<String> skills) {
        String tablelisting = "";
        if (skills.size() > 0) {
            for (int i = 0; i < skills.size(); i++) {
                String element = (String) skills.get(i);
                element = element.replaceAll(" OR ", " or<br> ");
                tablelisting = tablelisting.concat("<tr><td style=\"vertical-align: top;\">" + element + "</td></tr>");
            }
        } else {
            tablelisting = "<tr><td style=\"vertical-align: top;\"></td></tr>";
        }
        return tablelisting;
    }

    static String update_sheet_front(Character character) throws IOException {
        String charactersheet = "";
        String characterSheetFront = System.getProperty("user.dir");
        characterSheetFront = characterSheetFront.concat("/dataFiles/html/front.html");
        try {
            FileInputStream fis = new FileInputStream(characterSheetFront);
            FileChannel fc = fis.getChannel();
            MappedByteBuffer mbf = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            byte[] barray = new byte[(int) (fc.size())];
            mbf.get(barray);
            charactersheet = new String(barray);
        } finally {
        }
        charactersheet = charactersheet.replaceAll("_NAME_", character.name);
        charactersheet = charactersheet.replaceAll("_RACE_", character.race);
        charactersheet = charactersheet.replaceAll("_SEX_", character.gender);
        charactersheet = charactersheet.replaceAll("_HEIGHT_", intsStrings.toString(character.height));
        charactersheet = charactersheet.replaceAll("_WEIGHT_", intsStrings.toString(character.weight));
        charactersheet = charactersheet.replaceAll("_AGE_", intsStrings.toString(character.age));
        charactersheet = charactersheet.replaceAll("_HAIRCOLOR_", character.haircolor);
        charactersheet = charactersheet.replaceAll("_HAIRTYPE_", character.hairtype);
        charactersheet = charactersheet.replaceAll("_EYECOLOR_", character.eyecolor);
        charactersheet = charactersheet.replaceAll("_BIRTHPLACE_", character.birthplace);
        charactersheet = charactersheet.replaceAll("_STARSIGN_", character.starsign);
        charactersheet = charactersheet.replaceAll("_MARKS_", character.marks);
        charactersheet = charactersheet.replaceAll("_SIBLINGS_", intsStrings.toString(character.siblings));
        charactersheet = charactersheet.replaceAll("_CURRENT_CAREER_", character.career);
        charactersheet = charactersheet.replaceAll("_CAREER_PATH_", intsStrings.toString(character.career_path));
        charactersheet = charactersheet.replaceAll("_SWS_", intsStrings.toString(character.starting_profile[0]));
        charactersheet = charactersheet.replaceAll("_SBS_", intsStrings.toString(character.starting_profile[1]));
        charactersheet = charactersheet.replaceAll("_SS_", intsStrings.toString(character.starting_profile[2]));
        charactersheet = charactersheet.replaceAll("_ST_", intsStrings.toString(character.starting_profile[3]));
        charactersheet = charactersheet.replaceAll("_SAG_", intsStrings.toString(character.starting_profile[4]));
        charactersheet = charactersheet.replaceAll("_SINT_", intsStrings.toString(character.starting_profile[5]));
        charactersheet = charactersheet.replaceAll("_SWP_", intsStrings.toString(character.starting_profile[6]));
        charactersheet = charactersheet.replaceAll("_SFEL_", intsStrings.toString(character.starting_profile[7]));
        charactersheet = charactersheet.replaceAll("_SA_", intsStrings.toString(character.starting_profile[8]));
        charactersheet = charactersheet.replaceAll("_SW_", intsStrings.toString(character.starting_profile[9]));
        charactersheet = charactersheet.replaceAll("_SSB_", intsStrings.toString(character.starting_profile[10]));
        charactersheet = charactersheet.replaceAll("_STB_", intsStrings.toString(character.starting_profile[11]));
        charactersheet = charactersheet.replaceAll("_SM_", intsStrings.toString(character.starting_profile[12]));
        charactersheet = charactersheet.replaceAll("_SMAG_", intsStrings.toString(character.starting_profile[13]));
        charactersheet = charactersheet.replaceAll("_SIP_", intsStrings.toString(character.starting_profile[14]));
        charactersheet = charactersheet.replaceAll("_SFP_", intsStrings.toString(character.starting_profile[15]));
        charactersheet = charactersheet.replaceAll("_AWS_", intsStrings.toString(character.advance_scheme[0]));
        charactersheet = charactersheet.replaceAll("_ABS_", intsStrings.toString(character.advance_scheme[1]));
        charactersheet = charactersheet.replaceAll("_AS_", intsStrings.toString(character.advance_scheme[2]));
        charactersheet = charactersheet.replaceAll("_AT_", intsStrings.toString(character.advance_scheme[3]));
        charactersheet = charactersheet.replaceAll("_AAG_", intsStrings.toString(character.advance_scheme[4]));
        charactersheet = charactersheet.replaceAll("_AINT_", intsStrings.toString(character.advance_scheme[5]));
        charactersheet = charactersheet.replaceAll("_AWP_", intsStrings.toString(character.advance_scheme[6]));
        charactersheet = charactersheet.replaceAll("_AFEL_", intsStrings.toString(character.advance_scheme[7]));
        charactersheet = charactersheet.replaceAll("_AA_", intsStrings.toString(character.advance_scheme[8]));
        charactersheet = charactersheet.replaceAll("_AW_", intsStrings.toString(character.advance_scheme[9]));
        charactersheet = charactersheet.replaceAll("_AM_", intsStrings.toString(character.advance_scheme[12]));
        charactersheet = charactersheet.replaceAll("_AMAG_", intsStrings.toString(character.advance_scheme[13]));
        charactersheet = charactersheet.replaceAll("_TWS_", intsStrings.toString(character.advance_taken[0]));
        charactersheet = charactersheet.replaceAll("_TBS_", intsStrings.toString(character.advance_taken[1]));
        charactersheet = charactersheet.replaceAll("_TS_", intsStrings.toString(character.advance_taken[2]));
        charactersheet = charactersheet.replaceAll("_TT_", intsStrings.toString(character.advance_taken[3]));
        charactersheet = charactersheet.replaceAll("_TAG_", intsStrings.toString(character.advance_taken[4]));
        charactersheet = charactersheet.replaceAll("_TINT_", intsStrings.toString(character.advance_taken[5]));
        charactersheet = charactersheet.replaceAll("_TWP_", intsStrings.toString(character.advance_taken[6]));
        charactersheet = charactersheet.replaceAll("_TFEL_", intsStrings.toString(character.advance_taken[7]));
        charactersheet = charactersheet.replaceAll("_TA_", intsStrings.toString(character.advance_taken[8]));
        charactersheet = charactersheet.replaceAll("_TW_", intsStrings.toString(character.advance_taken[9]));
        charactersheet = charactersheet.replaceAll("_TM_", intsStrings.toString(character.advance_taken[12]));
        charactersheet = charactersheet.replaceAll("_TMAG_", intsStrings.toString(character.advance_taken[13]));
        charactersheet = charactersheet.replaceAll("_CWS_", intsStrings.toString(character.current_profile[0] + character.talent_bonus[0]));
        charactersheet = charactersheet.replaceAll("_CBS_", intsStrings.toString(character.current_profile[1] + character.talent_bonus[1]));
        charactersheet = charactersheet.replaceAll("_CS_", intsStrings.toString(character.current_profile[2] + character.talent_bonus[2]));
        charactersheet = charactersheet.replaceAll("_CT_", intsStrings.toString(character.current_profile[3] + character.talent_bonus[3]));
        charactersheet = charactersheet.replaceAll("_CAG_", intsStrings.toString(character.current_profile[4] + character.talent_bonus[4]));
        charactersheet = charactersheet.replaceAll("_CINT_", intsStrings.toString(character.current_profile[5] + character.talent_bonus[5]));
        charactersheet = charactersheet.replaceAll("_CWP_", intsStrings.toString(character.current_profile[6] + character.talent_bonus[6]));
        charactersheet = charactersheet.replaceAll("_CFEL_", intsStrings.toString(character.current_profile[7 + character.talent_bonus[7]]));
        charactersheet = charactersheet.replaceAll("_CA_", intsStrings.toString(character.current_profile[8 + character.talent_bonus[8]]));
        charactersheet = charactersheet.replaceAll("_CW_", intsStrings.toString(character.current_profile[9 + character.talent_bonus[9]]));
        charactersheet = charactersheet.replaceAll("_CSB_", intsStrings.toString(character.current_profile[2] / 10));
        charactersheet = charactersheet.replaceAll("_CTB_", intsStrings.toString(character.current_profile[3] / 10));
        charactersheet = charactersheet.replaceAll("_CM_", intsStrings.toString(character.current_profile[12 + character.talent_bonus[12]]));
        charactersheet = charactersheet.replaceAll("_CMAG_", intsStrings.toString(character.current_profile[13] + character.talent_bonus[13]));
        charactersheet = charactersheet.replaceAll("_CIP_", intsStrings.toString(character.current_profile[14 + character.talent_bonus[14]]));
        charactersheet = charactersheet.replaceAll("_CFP_", intsStrings.toString(character.current_profile[15 + character.talent_bonus[15]]));
        return charactersheet;
    }

    public static Object promptForOr(String value, String Type) {
        if (GeneralFunctions.checkForOR(value)) {
            String returnValue = popupOrOption(value, Type);
            return returnValue;
        } else {
            return value;
        }
    }

    private static String popupOrOption(String value, String Type) {
        Icon icon = null;
        JFrame frame = null;
        Object[] possibilities = GeneralFunctions.getORs(value);
        String s = (String) JOptionPane.showInputDialog(frame, "Select a " + Type + " :\n", "Customized Dialog", JOptionPane.PLAIN_MESSAGE, icon, possibilities, possibilities[0]);
        if ((s != null) && (s.length() > 0)) {
            return s;
        }
        return null;
    }

    public static Character freeAdvance(Character character, String myCareer) {
        Icon icon = null;
        JFrame frame = null;
        Career career = new Career();
        career = Career.initilizeCareer(myCareer);
        Object[] possibilities = new Object[16];
        int j = 0;
        for (int i = 0; i < 8; i++) {
            int value = career.advance_scheme[i];
            if (value > 0) {
                if ((value / 5) > character.advance_taken[i]) {
                    String element = career.profile[i];
                    possibilities[j] = element;
                    j++;
                }
            }
        }
        for (int i = 8; i < 16; i++) {
            int value = career.advance_scheme[i];
            if (value > 0) {
                if (value > character.advance_taken[i]) {
                    String element = career.profile[i];
                    possibilities[j] = element;
                    j++;
                }
            }
        }
        String s = (String) JOptionPane.showInputDialog(frame, "Select your Free Advance :\n", "Customized Dialog", JOptionPane.PLAIN_MESSAGE, icon, possibilities, possibilities[0]);
        if ((s != null) && (s.length() > 0)) {
            int Modifier = 1;
            if (lookupAttrib(s) < 8) {
                Modifier = 5;
            }
            character.current_profile[lookupAttrib(s)] = character.current_profile[lookupAttrib(s)] + Modifier;
            character.advance_taken[lookupAttrib(s)]++;
            character.advance_scheme[lookupAttrib(s)] = character.advance_scheme[lookupAttrib(s)] - Modifier;
            return character;
        }
        int Modifier = 1;
        if (lookupAttrib(s) < 8) {
            Modifier = 5;
        }
        character.current_profile[lookupAttrib(s)] = character.current_profile[lookupAttrib(s)] + Modifier;
        character.advance_taken[lookupAttrib(s)]++;
        character.advance_scheme[lookupAttrib(s)] = character.advance_scheme[lookupAttrib(s)] - Modifier;
        return character;
    }

    public static Object promptForAny(Object value, String type) {
        Icon icon = null;
        JFrame frame = null;
        String myANY = ((String) value).replace(" (ANY)", "");
        Object[] possibilities = IOFunctions.getAnyList(myANY);
        String s = (String) JOptionPane.showInputDialog(frame, "Select a " + type + " :\n", "Customized Dialog", JOptionPane.PLAIN_MESSAGE, icon, possibilities, possibilities[0]);
        if ((s != null) && (s.length() > 0)) {
            return s;
        }
        return null;
    }

    public static Object promptForSM(int[] myStats) {
        Icon icon = null;
        JFrame frame = null;
        int zeros = countZeros(myStats);
        Object[] possibilities = new Object[8 - zeros];
        int count = 0;
        for (int i = 0; i < 8; i++) {
            if (myStats[i] > 0) {
                possibilities[count] = "Increase " + get_attribute.convert(i) + " to " + myStats[i];
                count++;
            }
        }
        if (count > 0) {
            String s = (String) JOptionPane.showInputDialog(frame, "Shallya has Mercy - Choose an attribute to increase :\n", "Customized Dialog", JOptionPane.PLAIN_MESSAGE, icon, possibilities, possibilities[0]);
            if ((s != null) && (s.length() > 0)) {
                return s;
            }
        } else {
            JOptionPane.showMessageDialog(null, "Shallya has no Mercy for you!", "Message Dialog", JOptionPane.PLAIN_MESSAGE);
        }
        return "nothing";
    }

    private static int countZeros(int[] array) {
        int count = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] == 0) {
                count++;
            }
        }
        return count;
    }

    public static void showAbout() {
        String Title = "WFRP-CharGen version beta r38\n";
        String Website = "http://code.google.com/p/wfrp-chargen/ \n";
        String Disclaimer = "Warhammer and other Warhammer Fantasy Rople Play (and the Logo's) are \n" + "(probably registered) trademarks of Games Workshop and/or Green Ronin and/or Black \n" + "Industries and/or Fantasy Flight Games.  The use of trademarks and materials are not \n" + "meant as a challange to their rights and is not intended to make or lose any money \n" + "for anyone. ";
        String Message = Title + "\n" + Website + "\n" + Disclaimer;
        JOptionPane.showMessageDialog(null, Message, "Message Dialog", JOptionPane.PLAIN_MESSAGE);
    }

    public static void showNotEnoughExp() {
        String Message = "You do not have enough Exp";
        JOptionPane.showMessageDialog(null, Message, "Message Dialog", JOptionPane.PLAIN_MESSAGE);
    }
}
