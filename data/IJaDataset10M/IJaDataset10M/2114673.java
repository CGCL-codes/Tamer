package com.piece_framework.yaml_editor.ui.editor;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.ITextViewer;

/**
 * YAML Editor のダブルクリック時動作クラス.
 * ダブルクリック時の動作を決定する。
 * 
 * @author Hideharu Matsufuji
 * @version 0.1.0
 * @since 0.1.0
 * @see org.eclipse.jface.text.ITextDoubleClickStrategy
 * 
 */
public class YAMLDoubleClickStrategy implements ITextDoubleClickStrategy {

    private ITextViewer fText;

    /**
     * ダブルクリックイベント処理.
     * 単語単位で選択状態にする。
     * 
     * @param part 対象ビュー
     * @see org.eclipse.jface.text.ITextDoubleClickStrategy
     *          #doubleClicked(org.eclipse.jface.text.ITextViewer)
     */
    public void doubleClicked(ITextViewer part) {
        int pos = part.getSelectedRange().x;
        if (pos < 0) {
            return;
        }
        fText = part;
        selectWord(pos);
    }

    /**
     * 単語を選択状態にする.
     * 
     * @param caretPos 現在のカーソル位置
     * @return 選択状態にした場合はtrue
     */
    protected boolean selectWord(int caretPos) {
        IDocument doc = fText.getDocument();
        int startPos, endPos;
        try {
            int pos = caretPos;
            char c;
            while (pos >= 0) {
                c = doc.getChar(pos);
                if (!Character.isJavaIdentifierPart(c)) {
                    break;
                }
                --pos;
            }
            startPos = pos;
            pos = caretPos;
            int length = doc.getLength();
            while (pos < length) {
                c = doc.getChar(pos);
                if (!Character.isJavaIdentifierPart(c)) {
                    break;
                }
                ++pos;
            }
            endPos = pos;
            if (startPos != endPos) {
                selectRange(startPos, endPos);
            }
            return true;
        } catch (BadLocationException x) {
        }
        return false;
    }

    /**
     * 指定された範囲を選択状態にする.
     * 
     * @param startPos 開始位置
     * @param stopPos 終了位置
     */
    private void selectRange(int startPos, int stopPos) {
        int offset = startPos + 1;
        int length = stopPos - offset;
        fText.setSelectedRange(offset, length);
    }
}
