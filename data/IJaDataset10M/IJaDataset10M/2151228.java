package net.sf.repairslab;

import java.io.File;
import net.sf.repairslab.control.CommonMetodBin;

public class EnvConstants {

    public static String LAST_REVISION_CHECK_URL = "http://repairslab.sourceforge.net/lastVersion.properties";

    public static String LINK_DONATE_IT = "http://repairslab.sourceforge.net/donationIt.php";

    public static String LINK_DONATE_EN = "http://repairslab.sourceforge.net/donationEn.php";

    public static String LINK_SUPPORT_REQUEST = "http://sourceforge.net/tracker2/?atid=1116456&group_id=241576&func=browse";

    public static String LINK_WEBSITE = "http://repairslab.sourceforge.net/";

    public static String LINK_BUG_REPORT = "http://sourceforge.net/tracker2/?group_id=241576&atid=1116455";

    public static String LINK_DOWNLOAD = "https://sourceforge.net/projects/repairslab/files/";

    public static String LINK_TEAM = "http://repairslab.sourceforge.net/index.php?option=com_content&view=article&id=10&Itemid=20";

    public static String FILE_USER_GUIDE_PREFIX = "documents" + File.separator + CommonMetodBin.getInstance().getCurrentRelease().getVersion() + File.separator + "UserGuide" + File.separator + "pdf" + File.separator + "RepairsLabUserGuide-";

    public static String FILE_USER_GUIDE_SUFFIX = ".pdf";

    public static String USER_HOME_DIR = System.getProperty("user.home") + File.separator + ".repairslab";

    public static String PORTABLE_HOME_DIR = "metadata";
}
