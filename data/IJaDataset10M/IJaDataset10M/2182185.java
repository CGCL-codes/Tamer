package org.jazzteam.EncodingWords;

/**
 * 
 * @author Karan
 * 
 *         �������� �������� ����, � ������ ���������������
 */
public class EncodingWord {

    private String encodingLine;

    public EncodingWord(String encodingLine) {
        this.encodingLine = encodingLine;
    }

    public String translateEvenElements() {
        String supportLine = new String();
        String outerLine = new String();
        String lineArray[] = encodingLine.split(" ");
        for (int i = 0; i < lineArray.length; i++) {
            if ((i + 1) % 2 == 0) {
                for (int k = lineArray[i].length() - 1; k >= 0; k--) {
                    supportLine += lineArray[i].charAt(k);
                }
                outerLine += supportLine + " ";
                supportLine = "";
            } else lineArray[i] = "";
        }
        return outerLine;
    }
}
