package br.com.jonasluz.velhinha;

import java.util.Vector;

/**
 * Tabuleiro do jogo. 
 * @author Jonas Luz <dev@jonasluz.com>
 * @version 0.3.0.1
 */
public class GameTable {

    public static final byte ID_PLAYER_X = 0;

    public static final byte ID_PLAYER_O = 1;

    public static final byte ID_NONE = -1;

    public static final byte ID_DRAW = -2;

    public static final char[] SYMBOL = new char[] { 'X', 'O' };

    private short[] tables;

    public GameTable() {
        init();
    }

    /**
	 * inicia o tabuleiro.
	 */
    public void init() {
        tables = new short[] { 0, 0 };
    }

    /**
	 * informa o código do outro jogador.
	 * @param current jogaor corrente.
	 * @return código do oponente ao corrente.
	 */
    public byte changePlayer(byte current) {
        return (byte) Math.abs(current - 1);
    }

    /**
	 * recupera estado do tabuleiro. 
	 * @return estado do tabuleiro - código do vencedor, empate ou nenhum (jogo em andamento).
	 */
    public byte getState() {
        for (int i = 0; i < Position.WIN_MASKS.length; i++) {
            short mask = Position.WIN_MASKS[i];
            if (maskFits(mask, ID_PLAYER_X)) return ID_PLAYER_X;
            if (maskFits(mask, ID_PLAYER_O)) return ID_PLAYER_O;
        }
        if (isFull()) return ID_DRAW;
        return ID_NONE;
    }

    /**
	 * Obtém o valor armazenado na posição indicada. 
	 * @param position Posição esperada.
	 * @return o valor do jogador na posição indicada.
	 */
    public byte getValue(byte position) {
        short mask = Position.getMask(position);
        if (maskFits(mask, ID_PLAYER_X)) return ID_PLAYER_X;
        if (maskFits(mask, ID_PLAYER_O)) return ID_PLAYER_O;
        return ID_NONE;
    }

    /**
	 * Marca o valor passado na posição indicada.
	 * @param position posição.
	 * @param value valor.
	 * @return se a marcação obteve sucesso. 
	 */
    public boolean setValue(byte position, byte player) {
        if (getValue(position) != ID_NONE) return false;
        int mask = Position.getMask(position);
        tables[player] = (short) (tables[player] | mask);
        return getValue(position) != ID_NONE;
    }

    /**
	 * Recupera as posições livres para jogo. 
	 * @return Vector de posições.
	 */
    public Vector getFreePositions() {
        Vector free = new Vector(1, 1);
        for (byte position = Position.MIN_POSITION; position <= Position.MAX_POSITION; position++) {
            if (getValue((byte) position) == ID_NONE) free.addElement(new Byte((byte) position));
        }
        return free;
    }

    /**
	 * informa se o tabuleiro está vazio.
	 * @return tabuleiro vazio? 
	 */
    public boolean isEmpty() {
        return (tables[ID_PLAYER_X] == 0) && (tables[ID_PLAYER_O] == 0);
    }

    /**
	 * informa se o tabuleiro está cheio. 
	 * @return tabuleiro cheio? 
	 */
    public boolean isFull() {
        return (tables[ID_PLAYER_X] ^ tables[ID_PLAYER_O]) == 0x1FF;
    }

    /**
	 * verifica se a máscara encaixa para o jogador especificado. 
	 * @param mask máscara binária.
	 * @param player código do jogador a verificar. 
	 */
    public boolean maskFits(short mask, byte player) {
        return (tables[player] & mask) == mask;
    }
}
