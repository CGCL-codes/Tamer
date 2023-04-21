package edu.infrabig.odf.read;

import java.io.File;
import java.io.IOException;
import javax.swing.table.TableModel;
import org.jopendocument.dom.OOUtils;
import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;
import edu.infrabig.odf.write.IWriterOdfFile;
import edu.infrabig.odf.write.WriterOdfImpl;
import anima.annotation.Component;
import anima.component.base.ComponentBase;

/**
 * 
 * Componenete para permitir ler arquivos odf.
 * 
 * @author Raimundo/Elmo
 * 
 */
@Component(id = "http://purl.org/NET/dcc/edu.infrabig.odf.read.ReadOdfFileImpl", provides = "http://purl.org/NET/edu.infrabig.odf.read.IReadOdfFile")
public class ReadOdfFileImpl extends ComponentBase implements IReadOdfFile {

    private IWriterOdfFile writeOdfFile;

    /**
	 * Abre um documento passado como par�metro para visualiza��o\edi��o do
	 * usu�rio.
	 * 
	 * @param pFile
	 *            Arquivo a ser aberto.
	 * @throws IOException
	 *             Poss�vel exce��o.
	 */
    public void readDocument(File pFile) throws IOException {
        openDocument(pFile);
    }

    /**
	 * Abrir um documento para edi��o\visualiza��o.
	 */
    @Override
    public void openDocument(File pFile) throws IOException {
        OOUtils.open(pFile);
    }

    /**
	 * Cria um documento novo.
	 */
    @Override
    public void createDocument(File pFile, TableModel pModel) throws IOException {
        SpreadSheet.createEmpty(pModel).saveAs(pFile);
    }

    @Override
    public void openDocument(File file, IWriterOdfFile writeDoc) throws IOException {
        if (!file.exists()) {
            writeDoc.createEmptyDocument(file);
        }
        openDocument(file);
    }
}
