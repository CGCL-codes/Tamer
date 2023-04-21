package lang2.vm;

import org.antlr.runtime.Token;

/**
 * Описывает расположение конструкции в тексте
 * @author gocha
 */
public interface SourceLocation {

    /**
     * Указывает начало правила
     * @return Лексема
     */
    Token getStart();

    /**
     * Указывает начало правила
     * @param start Лексема
     */
    void setStart(Token start);

    /**
     * Указывает конец правила
     * @return Лексема
     */
    Token getStop();

    /**
     * Указывает конец правила
     * @param stop Лексема
     */
    void setStop(Token stop);
}
