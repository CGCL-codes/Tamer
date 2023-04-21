package de.guhsoft.jinto.core.editor.table;

import org.eclipse.swt.graphics.Image;

/**
 * Gateway beetween the model and the table(view). 
 */
public interface IResourceTableGateway {

    /**
	 * Will be called when the text changed.
	 * 
	 * @param rowKey the row of the changed cell
	 * @param column the column of the changed cell
	 * @param newText the new text for the cell
	 * */
    void textChanged(String rowKey, String column, String newText);

    /**
	 * Will be called if there the user writes a duplicate key into the table.
	 * @param currentKey the key of the row(his old value)
	 * @param duplicateKey the duplicateKey(the new value of currentKey)
	 * */
    void duplicateKeyListening(String currentKey, String duplicateKey);

    /**
	 * Return the Image of the given row.
	 * */
    Image getImage(String rowKey);

    /**
	 * Return the text for the image in the given row.
	 * */
    String getTextForImage(String rowKey);

    /**
	 * Return the text for the given row and column.
	 * */
    String getText(String rowKey, String column);

    /**
	 * Return true if the text of the cell is virtual. Virtual text is the default 
	 * text which is displayed in cells with empty text when the row has default
	 * text and the user has enabled virtual text in the options.
	 * */
    boolean isVirtualText(String rowKey, String column);

    /**
	 * Return the identifier which should be used to get the saved values from
	 * the preferences.
	 * */
    String getPreferencesIdentifier();

    /**
	 * Return the calculated index for the new column. 
	 * */
    int getIndexForNewColumn(String columnText);
}
