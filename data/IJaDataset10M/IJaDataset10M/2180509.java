package br.eti.robsonfranca.clf4j.interfaces;

import br.eti.robsonfranca.clf4j.enums.ClearOp;
import br.eti.robsonfranca.clf4j.impl.Color;

public interface Console {

    void gotoxy(int x, int y);

    int getWidth();

    int getHeight();

    void clrscr(ClearOp clo);

    void setForeColor(Color color);

    void setBackColor(Color color);

    void printf(String format, Object... args);

    String[] scanf(String format);

    void moveUp(int n);

    void moveDown(int n);

    void moveLeft(int n);

    void moveRight(int n);

    void moveNextLine(int n);

    void movePrevLine(int n);

    void saveCurPos();

    void restCurPos();

    void scrollUp(int n);

    void scrollDown(int n);

    void clrLine(ClearOp clo);

    void sendANSI(String command);

    void showCursor();

    void hideCursor();

    char getChar();
}
