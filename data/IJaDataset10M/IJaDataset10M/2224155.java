package util.basedatatype.file.msword;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class MSWordManager {

    private Dispatch doc;

    private ActiveXComponent word;

    private Dispatch documents;

    private Dispatch selection;

    private boolean saveOnExit = true;

    /** 
	 *     
	 * @param visible Ϊtrue��ʾwordӦ�ó���ɼ� 
	 */
    public MSWordManager(boolean visible) {
        if (word == null) {
            word = new ActiveXComponent("Word.Application");
            word.setProperty("Visible", new Variant(visible));
        }
        if (documents == null) documents = word.getProperty("Documents").toDispatch();
    }

    /** 
	 * �����˳�ʱ���� 
	 *     
	 * @param saveOnExit boolean true-�˳�ʱ�����ļ���false-�˳�ʱ�������ļ� 
	 */
    public void setSaveOnExit(boolean saveOnExit) {
        this.saveOnExit = saveOnExit;
    }

    /** 
	 * ����һ���µ�word�ĵ� 
	 *     
	 */
    public void createNewDocument() {
        doc = Dispatch.call(documents, "Add").toDispatch();
        selection = Dispatch.get(word, "Selection").toDispatch();
    }

    /** 
	 * ��һ���Ѵ��ڵ��ĵ� 
	 *     
	 * @param docPath 
	 */
    public void openDocument(String docPath) {
        closeDocument();
        doc = Dispatch.call(documents, "Open", docPath).toDispatch();
        selection = Dispatch.get(word, "Selection").toDispatch();
    }

    /** 
	 * ��ѡ�������ݻ����������ƶ� 
	 *     
	 * @param pos �ƶ��ľ��� 
	 */
    public void moveUp(int pos) {
        if (selection == null) selection = Dispatch.get(word, "Selection").toDispatch();
        for (int i = 0; i < pos; i++) Dispatch.call(selection, "MoveUp");
    }

    /** 
	 * ��ѡ�������ݻ��߲���������ƶ� 
	 *     
	 * @param pos �ƶ��ľ��� 
	 */
    public void moveDown(int pos) {
        if (selection == null) selection = Dispatch.get(word, "Selection").toDispatch();
        for (int i = 0; i < pos; i++) Dispatch.call(selection, "MoveDown");
    }

    /** 
	 * ��ѡ�������ݻ��߲���������ƶ� 
	 *     
	 * @param pos �ƶ��ľ��� 
	 */
    public void moveLeft(int pos) {
        if (selection == null) selection = Dispatch.get(word, "Selection").toDispatch();
        for (int i = 0; i < pos; i++) {
            Dispatch.call(selection, "MoveLeft");
        }
    }

    /** 
	 * ��ѡ�������ݻ��߲���������ƶ� 
	 *     
	 * @param pos �ƶ��ľ��� 
	 */
    public void moveRight(int pos) {
        if (selection == null) selection = Dispatch.get(word, "Selection").toDispatch();
        for (int i = 0; i < pos; i++) Dispatch.call(selection, "MoveRight");
    }

    /** 
	 * �Ѳ�����ƶ����ļ���λ�� 
	 *     
	 */
    public void moveStart() {
        if (selection == null) selection = Dispatch.get(word, "Selection").toDispatch();
        Dispatch.call(selection, "HomeKey", new Variant(6));
    }

    public void moveEnd() {
        if (selection == null) selection = Dispatch.get(word, "Selection").toDispatch();
        Dispatch.call(selection, "EndKey", new Variant(6));
    }

    /** 
	 * ��ѡ�����ݻ����㿪ʼ�����ı� 
	 *     
	 * @param toFindText Ҫ���ҵ��ı� 
	 * @return boolean true-���ҵ���ѡ�и��ı���false-δ���ҵ��ı� 
	 */
    public boolean find(String toFindText) {
        if (toFindText == null || toFindText.equals("")) return false;
        Dispatch find = word.call(selection, "Find").toDispatch();
        Dispatch.put(find, "Text", toFindText);
        Dispatch.put(find, "Forward", "True");
        Dispatch.put(find, "Format", "True");
        Dispatch.put(find, "MatchCase", "True");
        Dispatch.put(find, "MatchWholeWord", "True");
        return Dispatch.call(find, "Execute").getBoolean();
    }

    /** 
	 * ��ѡ��ѡ�������趨Ϊ�滻�ı� 
	 *     
	 * @param toFindText �����ַ� 
	 * @param newText Ҫ�滻������ 
	 * @return 
	 */
    public boolean replaceText(String toFindText, String newText) {
        if (!find(toFindText)) return false;
        Dispatch.put(selection, "Text", newText);
        return true;
    }

    /** 
	 * ȫ���滻�ı� 
	 *     
	 * @param toFindText �����ַ� 
	 * @param newText Ҫ�滻������ 
	 */
    public void replaceAllText(String toFindText, String newText) {
        while (find(toFindText)) {
            Dispatch.put(selection, "Text", newText);
            Dispatch.call(selection, "MoveRight");
        }
    }

    /** 
	 * �ڵ�ǰ���������ַ� 
	 *     
	 * @param newText Ҫ��������ַ� 
	 */
    public void insertText(String newText) {
        Dispatch.put(selection, "Text", newText);
    }

    /** 
	 *     
	 * @param toFindText Ҫ���ҵ��ַ� 
	 * @param imagePath ͼƬ·�� 
	 * @return 
	 */
    public boolean replaceImage(String toFindText, String imagePath) {
        if (!find(toFindText)) return false;
        Dispatch.call(Dispatch.get(selection, "InLineShapes").toDispatch(), "AddPicture", imagePath);
        return true;
    }

    /** 
	 * ȫ���滻ͼƬ 
	 *     
	 * @param toFindText �����ַ� 
	 * @param imagePath ͼƬ·�� 
	 */
    public void replaceAllImage(String toFindText, String imagePath) {
        while (find(toFindText)) {
            Dispatch.call(Dispatch.get(selection, "InLineShapes").toDispatch(), "AddPicture", imagePath);
            Dispatch.call(selection, "MoveRight");
        }
    }

    /** 
	 * �ڵ�ǰ��������ͼƬ 
	 *     
	 * @param imagePath ͼƬ·�� 
	 */
    public void insertImage(String imagePath) {
        Dispatch.call(Dispatch.get(selection, "InLineShapes").toDispatch(), "AddPicture", imagePath);
    }

    /** 
	 * �ϲ���Ԫ�� 
	 *     
	 * @param tableIndex 
	 * @param fstCellRowIdx 
	 * @param fstCellColIdx 
	 * @param secCellRowIdx 
	 * @param secCellColIdx 
	 */
    public void mergeCell(int tableIndex, int fstCellRowIdx, int fstCellColIdx, int secCellRowIdx, int secCellColIdx) {
        Dispatch tables = Dispatch.get(doc, "Tables").toDispatch();
        Dispatch table = Dispatch.call(tables, "Item", new Variant(tableIndex)).toDispatch();
        Dispatch fstCell = Dispatch.call(table, "Cell", new Variant(fstCellRowIdx), new Variant(fstCellColIdx)).toDispatch();
        Dispatch secCell = Dispatch.call(table, "Cell", new Variant(secCellRowIdx), new Variant(secCellColIdx)).toDispatch();
        Dispatch.call(fstCell, "Merge", secCell);
    }

    /**
	 * ��ֵ�Ԫ��
	 * @param tabIndex
	 * @param rowIndex
	 * @param colIndex
	 * @param iRow
	 * @param iCol
	 * @throws Exception
	 */
    public void splitCell(int tabIndex, int rowIndex, int colIndex, int iRow, int iCol) throws Exception {
        Object oTables = Dispatch.get(doc, "Tables").toDispatch();
        Object oItem = Dispatch.call(oTables, "Item", String.valueOf(tabIndex)).toDispatch();
        Object cell = Dispatch.call(oItem, "Cell", String.valueOf(rowIndex), String.valueOf(colIndex)).toDispatch();
        Dispatch.call(cell, "Split", String.valueOf(iRow), String.valueOf(iCol));
    }

    /**
	   * ������Ƶ�����ĳһ��Ԫ����
	   * int tabIndex,
	   * @param xCell �к�
	   * @param yCell �к�
	   * @throws Exception
	   */
    public void moveToCell(int tabIndex, int xCell, int yCell) throws Exception {
        Object oTables = Dispatch.get(doc, "Tables").toDispatch();
        Object oItem = Dispatch.call(oTables, "Item", String.valueOf(tabIndex)).toDispatch();
        Object cell = Dispatch.call(oItem, "Cell", String.valueOf(xCell), String.valueOf(yCell)).toDispatch();
        Dispatch.call(cell, "Select");
    }

    /**
	   * �ڵ�ǰ��괦��������
	   * @param text Ҫ���������
	   * @throws Exception
	   */
    public void write(String text) throws Exception {
        Dispatch.call(this.selection, "TypeText", text);
    }

    /**
	   * �ڵ�ǰ��괦�������֣�Ȼ����
	   * @param text String �ı�
	   * @throws Exception ����
	   */
    public void writeln(String text) throws Exception {
        write(text + "\r\n");
    }

    /** 
	 * ��ָ���ĵ�Ԫ������д��� 
	 *     
	 * @param tableIndex 
	 * @param cellRowIdx 
	 * @param cellColIdx 
	 * @param txt 
	 */
    public void putTxtToCell(int tableIndex, int cellRowIdx, int cellColIdx, String txt) {
        Dispatch tables = Dispatch.get(doc, "Tables").toDispatch();
        Dispatch table = Dispatch.call(tables, "Item", new Variant(tableIndex)).toDispatch();
        Dispatch cell = Dispatch.call(table, "Cell", new Variant(cellRowIdx), new Variant(cellColIdx)).toDispatch();
        Dispatch.call(cell, "Select");
        Dispatch.put(selection, "Text", txt);
    }

    /** 
	 * �ڵ�ǰ�ĵ�������� 
	 *     
	 * @param pos 
	 */
    public void copy(String toCopyText) {
        moveStart();
        if (this.find(toCopyText)) {
            Dispatch textRange = Dispatch.get(selection, "Range").toDispatch();
            Dispatch.call(textRange, "Copy");
        }
    }

    /** 
	 * �ڵ�ǰ�ĵ�ճ��������� 
	 *     
	 * @param pos 
	 */
    public void paste(String pos) {
        moveStart();
        if (this.find(pos)) {
            Dispatch textRange = Dispatch.get(selection, "Range").toDispatch();
            Dispatch.call(textRange, "Paste");
        }
    }

    /** 
	 * �ڵ�ǰ�ĵ�ָ����λ�ÿ������ 
	 *     
	 * @param pos ��ǰ�ĵ�ָ����λ�� 
	 * @param tableIndex �������ı����word�ĵ������λ�� 
	 */
    public void copyTable(String pos, int tableIndex) {
        Dispatch tables = Dispatch.get(doc, "Tables").toDispatch();
        Dispatch table = Dispatch.call(tables, "Item", new Variant(tableIndex)).toDispatch();
        Dispatch range = Dispatch.get(table, "Range").toDispatch();
        Dispatch.call(range, "Copy");
        if (this.find(pos)) {
            Dispatch textRange = Dispatch.get(selection, "Range").toDispatch();
            Dispatch.call(textRange, "Paste");
        }
    }

    /** 
	 * �ڵ�ǰ�ĵ�ĩβ����������һ���ĵ��еĶ��� 
	 *     
	 * @param anotherDocPath ��һ���ĵ��Ĵ���·�� 
	 * @param tableIndex �������Ķ�������һ���ĵ��е����(��1��ʼ) 
	 */
    public void copyParagraphFromAnotherDoc(String anotherDocPath, int paragraphIndex) {
        Dispatch wordContent = Dispatch.get(doc, "Content").toDispatch();
        Dispatch.call(wordContent, "InsertAfter", "$selection$");
        copyParagraphFromAnotherDoc(anotherDocPath, paragraphIndex, "$selection$");
    }

    /** 
	 * �ڵ�ǰ�ĵ�ָ����λ�ÿ���������һ���ĵ��еĶ��� 
	 *     
	 * @param anotherDocPath ��һ���ĵ��Ĵ���·�� 
	 * @param tableIndex �������Ķ�������һ���ĵ��е����(��1��ʼ) 
	 * @param pos ��ǰ�ĵ�ָ����λ�� 
	 */
    public void copyParagraphFromAnotherDoc(String anotherDocPath, int paragraphIndex, String pos) {
        Dispatch doc2 = null;
        try {
            doc2 = Dispatch.call(documents, "Open", anotherDocPath).toDispatch();
            Dispatch paragraphs = Dispatch.get(doc2, "Paragraphs").toDispatch();
            Dispatch paragraph = Dispatch.call(paragraphs, "Item", new Variant(paragraphIndex)).toDispatch();
            Dispatch range = Dispatch.get(paragraph, "Range").toDispatch();
            Dispatch.call(range, "Copy");
            if (this.find(pos)) {
                Dispatch textRange = Dispatch.get(selection, "Range").toDispatch();
                Dispatch.call(textRange, "Paste");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (doc2 != null) {
                Dispatch.call(doc2, "Close", new Variant(saveOnExit));
                doc2 = null;
            }
        }
    }

    /** 
	 * �ڵ�ǰ�ĵ�ָ����λ�ÿ���������һ���ĵ��еı�� 
	 *     
	 * @param anotherDocPath ��һ���ĵ��Ĵ���·�� 
	 * @param tableIndex �������ı������һ���ĵ��е����(��1��ʼ) 
	 * @param pos ��ǰ�ĵ�ָ����λ�� 
	 */
    public void copyTableFromAnotherDoc(String anotherDocPath, int tableIndex, String pos) {
        Dispatch doc2 = null;
        try {
            doc2 = Dispatch.call(documents, "Open", anotherDocPath).toDispatch();
            Dispatch tables = Dispatch.get(doc2, "Tables").toDispatch();
            Dispatch table = Dispatch.call(tables, "Item", new Variant(tableIndex)).toDispatch();
            Dispatch range = Dispatch.get(table, "Range").toDispatch();
            Dispatch.call(range, "Copy");
            if (this.find(pos)) {
                Dispatch textRange = Dispatch.get(selection, "Range").toDispatch();
                Dispatch.call(textRange, "Paste");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (doc2 != null) {
                Dispatch.call(doc2, "Close", new Variant(saveOnExit));
                doc2 = null;
            }
        }
    }

    /** 
	 * �ڵ�ǰ�ĵ�ָ����λ�ÿ���������һ���ĵ��е�ͼƬ 
	 *     
	 * @param anotherDocPath ��һ���ĵ��Ĵ���·�� 
	 * @param shapeIndex ��������ͼƬ����һ���ĵ��е�λ�� 
	 * @param pos ��ǰ�ĵ�ָ����λ�� 
	 */
    public void copyImageFromAnotherDoc(String anotherDocPath, int shapeIndex, String pos) {
        Dispatch doc2 = null;
        try {
            doc2 = Dispatch.call(documents, "Open", anotherDocPath).toDispatch();
            Dispatch shapes = Dispatch.get(doc2, "InLineShapes").toDispatch();
            Dispatch shape = Dispatch.call(shapes, "Item", new Variant(shapeIndex)).toDispatch();
            Dispatch imageRange = Dispatch.get(shape, "Range").toDispatch();
            Dispatch.call(imageRange, "Copy");
            if (this.find(pos)) {
                Dispatch textRange = Dispatch.get(selection, "Range").toDispatch();
                Dispatch.call(textRange, "Paste");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (doc2 != null) {
                Dispatch.call(doc2, "Close", new Variant(saveOnExit));
                doc2 = null;
            }
        }
    }

    /** 
	 * ������� 
	 *     
	 * @param pos    λ�� 
	 * @param cols ���� 
	 * @param rows ���� 
	 */
    public void createTable(int numCols, int numRows) {
        Dispatch tables = Dispatch.get(doc, "Tables").toDispatch();
        Dispatch range = Dispatch.get(selection, "Range").toDispatch();
        Dispatch newTable = Dispatch.call(tables, "Add", range, new Variant(numRows), new Variant(numCols)).toDispatch();
        Dispatch.call(selection, "MoveRight");
        moveEnd();
    }

    /** 
	 * ��ָ����ǰ�������� 
	 *     
	 * @param tableIndex word�ļ��еĵ�N�ű�(��1��ʼ) 
	 * @param rowIndex ָ���е����(��1��ʼ) 
	 */
    public void addTableRow(int tableIndex, int rowIndex) {
        Dispatch tables = Dispatch.get(doc, "Tables").toDispatch();
        Dispatch table = Dispatch.call(tables, "Item", new Variant(tableIndex)).toDispatch();
        Dispatch rows = Dispatch.get(table, "Rows").toDispatch();
        Dispatch row = Dispatch.call(rows, "Item", new Variant(rowIndex)).toDispatch();
        Dispatch.call(rows, "Add", new Variant(row));
    }

    /** 
	 * �ڵ�1��ǰ����һ�� 
	 *     
	 * @param tableIndex word�ĵ��еĵ�N�ű�(��1��ʼ) 
	 */
    public void addFirstTableRow(int tableIndex) {
        Dispatch tables = Dispatch.get(doc, "Tables").toDispatch();
        Dispatch table = Dispatch.call(tables, "Item", new Variant(tableIndex)).toDispatch();
        Dispatch rows = Dispatch.get(table, "Rows").toDispatch();
        Dispatch row = Dispatch.get(rows, "First").toDispatch();
        Dispatch.call(rows, "Add", new Variant(row));
    }

    /** 
	 * �����1��ǰ����һ�� 
	 *     
	 * @param tableIndex 
	 *                        word�ĵ��еĵ�N�ű�(��1��ʼ) 
	 */
    public void addLastTableRow(int tableIndex) {
        Dispatch tables = Dispatch.get(doc, "Tables").toDispatch();
        Dispatch table = Dispatch.call(tables, "Item", new Variant(tableIndex)).toDispatch();
        Dispatch rows = Dispatch.get(table, "Rows").toDispatch();
        Dispatch row = Dispatch.get(rows, "Last").toDispatch();
        Dispatch.call(rows, "Add", new Variant(row));
    }

    /** 
	 * ����һ�� 
	 *     
	 * @param tableIndex word�ĵ��еĵ�N�ű�(��1��ʼ) 
	 */
    public void addRow(int tableIndex) {
        Dispatch tables = Dispatch.get(doc, "Tables").toDispatch();
        Dispatch table = Dispatch.call(tables, "Item", new Variant(tableIndex)).toDispatch();
        Dispatch rows = Dispatch.get(table, "Rows").toDispatch();
        Dispatch.call(rows, "Add");
    }

    /** 
	 * ����һ�� 
	 *     
	 * @param tableIndex word�ĵ��еĵ�N�ű�(��1��ʼ) 
	 */
    public void addCol(int tableIndex) {
        Dispatch tables = Dispatch.get(doc, "Tables").toDispatch();
        Dispatch table = Dispatch.call(tables, "Item", new Variant(tableIndex)).toDispatch();
        Dispatch cols = Dispatch.get(table, "Columns").toDispatch();
        Dispatch.call(cols, "Add").toDispatch();
        Dispatch.call(cols, "AutoFit");
    }

    /** 
	 * ��ָ����ǰ�����ӱ����� 
	 *     
	 * @param tableIndex word�ĵ��еĵ�N�ű�(��1��ʼ) 
	 * @param colIndex    ָ���е���� (��1��ʼ) 
	 */
    public void addTableCol(int tableIndex, int colIndex) {
        Dispatch tables = Dispatch.get(doc, "Tables").toDispatch();
        Dispatch table = Dispatch.call(tables, "Item", new Variant(tableIndex)).toDispatch();
        Dispatch cols = Dispatch.get(table, "Columns").toDispatch();
        System.out.println(Dispatch.get(cols, "Count"));
        Dispatch col = Dispatch.call(cols, "Item", new Variant(colIndex)).toDispatch();
        Dispatch.call(cols, "Add", col).toDispatch();
        Dispatch.call(cols, "AutoFit");
    }

    /** 
	 * �ڵ�1��ǰ����һ�� 
	 *     
	 * @param tableIndex word�ĵ��еĵ�N�ű�(��1��ʼ) 
	 */
    public void addFirstTableCol(int tableIndex) {
        Dispatch tables = Dispatch.get(doc, "Tables").toDispatch();
        Dispatch table = Dispatch.call(tables, "Item", new Variant(tableIndex)).toDispatch();
        Dispatch cols = Dispatch.get(table, "Columns").toDispatch();
        Dispatch col = Dispatch.get(cols, "First").toDispatch();
        Dispatch.call(cols, "Add", col).toDispatch();
        Dispatch.call(cols, "AutoFit");
    }

    /** 
	 * �����һ��ǰ����һ�� 
	 *     
	 * @param tableIndex word�ĵ��еĵ�N�ű�(��1��ʼ) 
	 */
    public void addLastTableCol(int tableIndex) {
        Dispatch tables = Dispatch.get(doc, "Tables").toDispatch();
        Dispatch table = Dispatch.call(tables, "Item", new Variant(tableIndex)).toDispatch();
        Dispatch cols = Dispatch.get(table, "Columns").toDispatch();
        Dispatch col = Dispatch.get(cols, "Last").toDispatch();
        Dispatch.call(cols, "Add", col).toDispatch();
        Dispatch.call(cols, "AutoFit");
    }

    /** 
	 * �Զ������� 
	 *     
	 */
    public void autoFitTable() {
        Dispatch tables = Dispatch.get(doc, "Tables").toDispatch();
        int count = Dispatch.get(tables, "Count").toInt();
        for (int i = 0; i < count; i++) {
            Dispatch table = Dispatch.call(tables, "Item", new Variant(i + 1)).toDispatch();
            Dispatch cols = Dispatch.get(table, "Columns").toDispatch();
            Dispatch.call(cols, "AutoFit");
        }
    }

    /** 
	 * ����word��ĺ��Ե�����Ŀ��,���к걣����document�� 
	 *     
	 */
    public void callWordMacro() {
        Dispatch tables = Dispatch.get(doc, "Tables").toDispatch();
        int count = Dispatch.get(tables, "Count").toInt();
        Variant vMacroName = new Variant("Normal.NewMacros.tableFit");
        Variant vParam = new Variant("param1");
        Variant para[] = new Variant[] { vMacroName };
        for (int i = 0; i < para.length; i++) {
            Dispatch table = Dispatch.call(tables, "Item", new Variant(i + 1)).toDispatch();
            Dispatch.call(table, "Select");
            Dispatch.call(word, "Run", "tableFitContent");
        }
    }

    /** 
	 * ���õ�ǰѡ�����ݵ����� 
	 *     
	 * @param boldSize 
	 * @param italicSize 
	 * @param underLineSize �»��� 
	 * @param colorSize ������ɫ 
	 * @param size �����С 
	 * @param name ������� 
	 */
    public void setFont(boolean bold, boolean italic, boolean underLine, String colorSize, String size, String name) {
        Dispatch font = Dispatch.get(selection, "Font").toDispatch();
        Dispatch.put(font, "Name", new Variant(name));
        Dispatch.put(font, "Bold", new Variant(bold));
        Dispatch.put(font, "Italic", new Variant(italic));
        Dispatch.put(font, "Underline", new Variant(underLine));
        Dispatch.put(font, "Color", colorSize);
        Dispatch.put(font, "Size", size);
    }

    /** 
	 * �ļ���������Ϊ 
	 *     
	 * @param savePath ��������Ϊ·�� 
	 */
    public void save(String savePath) {
        Dispatch.call((Dispatch) Dispatch.call(word, "WordBasic").getDispatch(), "FileSaveAs", savePath);
    }

    /** 
	 * �رյ�ǰword�ĵ� 
	 *     
	 */
    public void closeDocument() {
        if (doc != null) {
            Dispatch.call(doc, "Save");
            Dispatch.call(doc, "Close", new Variant(saveOnExit));
            doc = null;
        }
    }

    /** 
	 * �ر�ȫ��Ӧ�� 
	 *     
	 */
    public void close() {
        closeDocument();
        if (word != null) {
            Dispatch.call(word, "Quit");
            word = null;
        }
        selection = null;
        documents = null;
    }

    /** 
	 * ��ӡ��ǰword�ĵ� 
	 *     
	 */
    public void printFile() {
        if (doc != null) {
            Dispatch.call(doc, "PrintOut");
        }
    }

    public String[] getTexts() {
        Dispatch wordContent = Dispatch.get(doc, "Content").toDispatch();
        Dispatch paragraphs = Dispatch.get(wordContent, "Paragraphs").toDispatch();
        int paragraphCount = Dispatch.get(paragraphs, "Count").getInt();
        if (paragraphCount <= 0) return null;
        String[] texts = new String[paragraphCount];
        for (int i = 1; i < paragraphCount; i++) {
            Dispatch paragraph = Dispatch.call(paragraphs, "Item", new Variant(i)).toDispatch();
            Dispatch paragraphRange = Dispatch.get(paragraph, "Range").toDispatch();
            String paragraphContent = Dispatch.get(paragraphRange, "Text").toString();
            texts[i] = paragraphContent;
        }
        return texts;
    }

    public static void main(String args[]) throws Exception {
        testDoc();
        MSWordManager msWordManager = new MSWordManager(true);
        msWordManager.createNewDocument();
        msWordManager.insertText("abcdefgadsaga$sds&agasdsagsagdsdsdsgds");
        msWordManager.moveEnd();
        msWordManager.moveStart();
        msWordManager.replaceText("&", "@@@");
        msWordManager.find("$");
        msWordManager.moveRight(3);
        msWordManager.find("&");
        ActiveXComponent wordApp = new ActiveXComponent("Word.Application");
        Dispatch.put(wordApp, "Visible", new Variant(true));
        Dispatch docs = wordApp.getProperty("Documents").toDispatch();
        Dispatch doc = Dispatch.call(docs, "Add").toDispatch();
        doc = msWordManager.doc;
        Dispatch wordContent = Dispatch.get(doc, "Content").toDispatch();
        Dispatch paragraphs = Dispatch.get(wordContent, "Paragraphs").toDispatch();
        int paragraphCount = Dispatch.get(paragraphs, "Count").getInt();
        System.out.println("paragraphCount:" + paragraphCount);
        for (int i = 1; i <= paragraphCount; i++) {
            Dispatch paragraph = Dispatch.call(paragraphs, "Item", new Variant(i)).toDispatch();
            Dispatch paragraphRange = Dispatch.get(paragraph, "Range").toDispatch();
            String paragraphContent = Dispatch.get(paragraphRange, "Text").toString();
            System.out.println(paragraphContent);
        }
        msWordManager.close();
    }

    public static void testDoc() {
        MSWordManager mWord = new MSWordManager(true);
        mWord.createNewDocument();
        mWord.openDocument("D:\\working\\a.doc");
        String[] texts = mWord.getTexts();
        for (String text : texts) {
            System.out.println(text);
        }
    }
}
