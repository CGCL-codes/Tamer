package cu.ftpd.modules.zipscript.external;

import cu.ftpd.modules.zipscript.shell.ShellZipscript;

/**
 * @author captain
 * @version $Id: PzsNgZipscript.java 293 2009-03-04 20:07:38Z jevring $
 * @since 2008-okt-30 - 22:18:46
 */
public class PzsNgZipscript extends ShellZipscript {

    protected String path;

    public PzsNgZipscript(String path) {
        this.path = path;
        if (!this.path.endsWith("/")) {
            this.path += "/";
        }
        this.rescanPath = this.path + "rescan";
        this.uploadPath = this.path + "zipscript-c";
        this.deletePath = this.path + "postdel";
    }
}
