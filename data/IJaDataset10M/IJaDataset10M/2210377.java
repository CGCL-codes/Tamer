package memodivx.memolution;

/**
 * Memodivx helps you managing your film database.
 * Copyright (C) 2004  Yann Biancheri, Thomas Rollinger
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * @author BIANCHERI Yann
 *
 */
public class LexicalUnit {

    public static final char WORD = 'w';

    public static final char OPEN = '(';

    public static final char CLOSE = ')';

    public static final char NOT = '!';

    public static final char AND = '&';

    public static final char OR = '|';

    /**
     * A char that indicates the type of the LU
     */
    private char type;

    /**
     * The word associate with the LU if the type is WORD
     */
    private String word;

    /**
     * Build a new LexicalUnit of this type
     * @param type the type of the LU
     */
    public LexicalUnit(char type) {
        this.type = type;
    }

    /**
     * Build a LU from the word of the type WORD
     * @param word 
     */
    public LexicalUnit(String word) {
        this.word = word;
        type = WORD;
    }

    /**
     * @return the LU is a word
     */
    public boolean isWord() {
        return type == WORD;
    }

    /**
     * PRECONTION: the LU is of type WORD
     * @return the word
     */
    public String getWord() {
        return word;
    }

    /**
     * @return the LU is an operator
     */
    public boolean isOperator() {
        return type == AND || type == OR || type == NOT;
    }

    /**
     * @return the type of the LU
     */
    public char getType() {
        return type;
    }

    public String toString() {
        String s = type + " ";
        if (type == 'w') s += word;
        return s;
    }
}
