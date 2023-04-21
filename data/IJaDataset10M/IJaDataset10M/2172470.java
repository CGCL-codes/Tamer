package fi.hiit.cutehip.packet;

import fi.hiit.framework.utils.Helpers;

public class SolutionParameter extends HipParameter {

    public static final short TYPE_SOLUTION = 0x141;

    public static final short PARAM_LENGTH = 0x14;

    public static final short BITS_VERIFIED_OFFSET = 0x04;

    public static final short OPAQUE_OFFSET = 0x06;

    public static final short PUZZLE_OFFSET = 0x08;

    public static final short SOLUTION_OFFSET = 0x10;

    public static final short PUZZLE_LENGTH = 0x08;

    public SolutionParameter(int size) {
        super(SolutionParameter.PARAM_LENGTH + HipParameter.PARAM_HEADER_OFFSET);
        __setType();
        __setLength();
    }

    public void setBitsVerified(int bits) {
        __data[BITS_VERIFIED_OFFSET] = (byte) (bits & 0xFF);
    }

    public byte getBitsVerified() {
        return __data[BITS_VERIFIED_OFFSET];
    }

    public void setOpaque(short opaque) {
        __data[LENGTH_OFFSET] = (byte) ((PARAM_LENGTH >> 8) & 0xFF);
        __data[LENGTH_OFFSET + 1] = (byte) (PARAM_LENGTH & 0xFF);
    }

    public long getPuzzle() {
        return (((__data[PUZZLE_OFFSET] & 0xFFFFFFFF) << 56) | ((__data[PUZZLE_OFFSET + 1] & 0xFFFFFFFF) << 48) | ((__data[PUZZLE_OFFSET + 2] & 0xFFFFFFFF) << 40) | ((__data[PUZZLE_OFFSET + 3] & 0xFFFFFFFF) << 32) | ((__data[PUZZLE_OFFSET + 4] & 0xFFFFFFFF) << 24) | ((__data[PUZZLE_OFFSET + 5] & 0xFFFFFFFF) << 16) | ((__data[PUZZLE_OFFSET + 6] & 0xFFFFFFFF) << 8) | (__data[PUZZLE_OFFSET + 7] & 0xFFFFFFFF));
    }

    public void setPuzzle(long puzzle) {
        __data[PUZZLE_OFFSET] = (byte) ((puzzle >> 56) & 0xFF);
        __data[PUZZLE_OFFSET + 1] = (byte) ((puzzle >> 48) & 0xFF);
        __data[PUZZLE_OFFSET + 2] = (byte) ((puzzle >> 40) & 0xFF);
        __data[PUZZLE_OFFSET + 3] = (byte) ((puzzle >> 32) & 0xFF);
        __data[PUZZLE_OFFSET + 4] = (byte) ((puzzle >> 24) & 0xFF);
        __data[PUZZLE_OFFSET + 5] = (byte) ((puzzle >> 16) & 0xFF);
        __data[PUZZLE_OFFSET + 6] = (byte) ((puzzle >> 8) & 0xFF);
        __data[PUZZLE_OFFSET + 7] = (byte) (puzzle & 0xFF);
    }

    public void setPuzzle(byte[] puzzle) {
        System.arraycopy(puzzle, 0, __data, PUZZLE_OFFSET, puzzle.length);
    }

    public long getSolution() {
        return (((__data[SOLUTION_OFFSET] & 0xFFFFFFFF) << 56) | ((__data[SOLUTION_OFFSET + 1] & 0xFFFFFFFF) << 48) | ((__data[SOLUTION_OFFSET + 2] & 0xFFFFFFFF) << 40) | ((__data[SOLUTION_OFFSET + 3] & 0xFFFFFFFF) << 32) | ((__data[SOLUTION_OFFSET + 4] & 0xFFFFFFFF) << 24) | ((__data[SOLUTION_OFFSET + 5] & 0xFFFFFFFF) << 16) | ((__data[SOLUTION_OFFSET + 6] & 0xFFFFFFFF) << 8) | (__data[SOLUTION_OFFSET + 7] & 0xFFFFFFFF));
    }

    public void getPuzzle(byte[] puzzle) {
        if (puzzle == null) return;
        System.arraycopy(__data, PUZZLE_OFFSET, puzzle, 0, PUZZLE_LENGTH);
    }

    public void getSolution(byte[] solution) {
        if (solution == null) return;
        System.arraycopy(__data, SOLUTION_OFFSET, solution, 0, PUZZLE_LENGTH);
    }

    public void setSolution(long solution) {
        __data[SOLUTION_OFFSET] = (byte) ((solution >> 56) & 0xFF);
        __data[SOLUTION_OFFSET + 1] = (byte) ((solution >> 48) & 0xFF);
        __data[SOLUTION_OFFSET + 2] = (byte) ((solution >> 40) & 0xFF);
        __data[SOLUTION_OFFSET + 3] = (byte) ((solution >> 32) & 0xFF);
        __data[SOLUTION_OFFSET + 4] = (byte) ((solution >> 24) & 0xFF);
        __data[SOLUTION_OFFSET + 5] = (byte) ((solution >> 16) & 0xFF);
        __data[SOLUTION_OFFSET + 6] = (byte) ((solution >> 8) & 0xFF);
        __data[SOLUTION_OFFSET + 7] = (byte) (solution & 0xFF);
    }

    public void setSolution(byte[] solution) {
        System.arraycopy(solution, 0, __data, SOLUTION_OFFSET, solution.length);
    }

    private void __setType() {
        __data[TYPE_OFFSET] = (byte) ((TYPE_SOLUTION >> 8) & 0xFF);
        __data[TYPE_OFFSET + 1] = (byte) (TYPE_SOLUTION & 0xFF);
    }

    private void __setLength() {
        __data[LENGTH_OFFSET] = (byte) ((PARAM_LENGTH >> 8) & 0xFF);
        __data[LENGTH_OFFSET + 1] = (byte) (PARAM_LENGTH & 0xFF);
    }
}
