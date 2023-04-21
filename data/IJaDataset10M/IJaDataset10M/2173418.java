package generate.fpacjava;

import generate.CBaseLanguageExporter;
import semantic.Verbs.CEntityCloseFile;
import utils.CObjectCatalog;

/**
 * @author S. Charton
 * @version $Id$
 */
public class CFPacJavaCloseFile extends CEntityCloseFile {

    /**
	 * @param line
	 * @param cat
	 * @param out
	 */
    public CFPacJavaCloseFile(int line, CObjectCatalog cat, CBaseLanguageExporter out) {
        super(line, cat, out);
    }

    /**
	 * @see semantic.CBaseLanguageEntity#DoExport()
	 */
    @Override
    protected void DoExport() {
        String cs = m_eFileDescriptor.ExportReference(getLine()) + ".close() ;";
        WriteLine(cs);
    }
}
