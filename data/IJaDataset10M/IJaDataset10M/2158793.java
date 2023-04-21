package it.chesslab.chessboard;

import it.chesslab.commons.StringLib;

/**  */
public final class CompletePosition implements Position {

    /**  */
    public static final String START_POSITION_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    /**  */
    private static final boolean CLONE = true;

    private static final boolean NO_CLONE = false;

    /**  */
    private final SimplePosition simplePosition;

    /**  */
    private int activeColor;

    /**  */
    private final boolean[][] isAvailableCastle;

    /**  */
    private int enpassantSquare;

    /**  */
    private int plyCount;

    private int moveNumber;

    /**  */
    private CompletePosition(SimplePosition chessboard, boolean clone, int activeColor, boolean[][] isAvailableCastle, int enpassantSquare, int plyCount, int moveNumber) {
        if (chessboard == null) {
            throw new IllegalArgumentException();
        }
        if (clone) {
            this.simplePosition = new SimplePosition(chessboard);
        } else {
            this.simplePosition = chessboard;
        }
        this.activeColor = activeColor;
        this.isAvailableCastle = new boolean[2][2];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                this.isAvailableCastle[i][j] = isAvailableCastle[i][j];
            }
        }
        this.enpassantSquare = enpassantSquare;
        this.plyCount = plyCount;
        this.moveNumber = moveNumber;
    }

    /**  */
    public CompletePosition() {
        this(new SimplePosition().setStartPosition(), NO_CLONE, Color.WHITE, new boolean[][] { { true, true }, { true, true } }, Chessboard.SQUARE_NONE, 0, 1);
    }

    /**  */
    public CompletePosition(SimplePosition chessboard, int activeColor, boolean[][] isAvailableCastle, int enpassantSquare, int plyCount, int moveNumber) {
        this(chessboard, CLONE, activeColor, isAvailableCastle, enpassantSquare, plyCount, moveNumber);
    }

    /**  */
    public CompletePosition(CompletePosition completePosition) {
        this(completePosition.simplePosition, CLONE, completePosition.activeColor, completePosition.isAvailableCastle, completePosition.enpassantSquare, completePosition.plyCount, completePosition.moveNumber);
    }

    /**  */
    public CompletePosition(String fen) {
        if (fen == null) {
            throw new IllegalArgumentException();
        }
        String[] fenPortions = StringLib.getTokens(fen, ' ');
        if (fenPortions.length != 6) {
            throw new IllegalArgumentException();
        }
        this.simplePosition = new SimplePosition();
        char[] chessboardChars = fenPortions[0].toCharArray();
        int i = 0;
        int n = 0;
        int row = 0;
        char ch;
        while (i < chessboardChars.length) {
            ch = chessboardChars[i++];
            switch(ch) {
                case 'K':
                case 'k':
                case 'Q':
                case 'q':
                case 'R':
                case 'r':
                case 'B':
                case 'b':
                case 'N':
                case 'n':
                case 'P':
                case 'p':
                    int square = (7 - n / 8) * 8 + n % 8;
                    n++;
                    if ((n - 1) / 8 != row) {
                        throw new IllegalArgumentException();
                    }
                    int type = Piece.getPieceType(Character.toUpperCase(ch));
                    int color = Character.isUpperCase(ch) ? Color.WHITE : Color.BLACK;
                    this.simplePosition.createPiece(square, type, color);
                    break;
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                    n = n + 8 - ('8' - ch);
                    if ((n - 1) / 8 != row) {
                        throw new IllegalArgumentException();
                    }
                    break;
                case '/':
                    if (n % 8 != 0) {
                        throw new IllegalArgumentException();
                    }
                    row++;
                    if (row > 7) {
                        throw new IllegalArgumentException();
                    }
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
        if (n != 64) {
            throw new IllegalArgumentException();
        }
        if (fenPortions[1].length() != 1) {
            throw new IllegalArgumentException();
        }
        switch(fenPortions[1].charAt(0)) {
            case 'w':
                this.activeColor = Color.WHITE;
                break;
            case 'b':
                this.activeColor = Color.BLACK;
                break;
            default:
                throw new IllegalArgumentException();
        }
        this.isAvailableCastle = new boolean[2][2];
        this.isAvailableCastle[Color.WHITE][Castle.SHORT_CASTLE] = false;
        this.isAvailableCastle[Color.WHITE][Castle.LONG_CASTLE] = false;
        this.isAvailableCastle[Color.BLACK][Castle.SHORT_CASTLE] = false;
        this.isAvailableCastle[Color.BLACK][Castle.LONG_CASTLE] = false;
        if (!fenPortions[2].equals("-")) {
            char[] chars = fenPortions[2].toCharArray();
            i = 0;
            if (i < chars.length && chars[i] == 'K') {
                this.isAvailableCastle[Color.WHITE][Castle.SHORT_CASTLE] = true;
                i++;
            }
            if (i < chars.length && chars[i] == 'Q') {
                this.isAvailableCastle[Color.WHITE][Castle.LONG_CASTLE] = true;
                i++;
            }
            if (i < chars.length && chars[i] == 'k') {
                this.isAvailableCastle[Color.BLACK][Castle.SHORT_CASTLE] = true;
                i++;
            }
            if (i < chars.length && chars[i] == 'q') {
                this.isAvailableCastle[Color.BLACK][Castle.LONG_CASTLE] = true;
                i++;
            }
            if (i == 0 || i != chars.length) {
                throw new IllegalArgumentException();
            }
        }
        this.enpassantSquare = Chessboard.SQUARE_NONE;
        if (!fenPortions[3].equals("-")) {
            char[] chars = fenPortions[3].toCharArray();
            if (chars.length != 2) {
                throw new IllegalArgumentException();
            }
            char letter = chars[0];
            char number = chars[1];
            if (letter < 'a' || letter > 'h' || number < '1' || number > '8') {
                throw new IllegalArgumentException();
            }
            this.enpassantSquare = Chessboard.getSquare(letter, number);
        }
        this.plyCount = Integer.parseInt(fenPortions[4], 10);
        this.moveNumber = Integer.parseInt(fenPortions[5], 10);
    }

    /**  */
    final void executeMove(Move move) {
        int fromSquare = move.getFromSquare();
        int toSquare = move.getToSquare();
        int pieceType = this.simplePosition.getPiece(fromSquare).getType();
        boolean isCapture = this.simplePosition.getPiece(toSquare) != null;
        MoveExecutor.executeMove(this.simplePosition, move);
        if (pieceType == Piece.KING) {
            this.isAvailableCastle[this.activeColor][Castle.SHORT_CASTLE] = false;
            this.isAvailableCastle[this.activeColor][Castle.LONG_CASTLE] = false;
        }
        if (fromSquare == Chessboard.SQUARE_H1 || toSquare == Chessboard.SQUARE_H1) {
            this.isAvailableCastle[Color.WHITE][Castle.SHORT_CASTLE] = false;
        }
        if (fromSquare == Chessboard.SQUARE_A1 || toSquare == Chessboard.SQUARE_A1) {
            this.isAvailableCastle[Color.WHITE][Castle.LONG_CASTLE] = false;
        }
        if (fromSquare == Chessboard.SQUARE_H8 || toSquare == Chessboard.SQUARE_H8) {
            this.isAvailableCastle[Color.BLACK][Castle.SHORT_CASTLE] = false;
        }
        if (fromSquare == Chessboard.SQUARE_A8 || toSquare == Chessboard.SQUARE_A8) {
            this.isAvailableCastle[Color.BLACK][Castle.LONG_CASTLE] = false;
        }
        this.enpassantSquare = Chessboard.SQUARE_NONE;
        if (pieceType == Piece.PAWN && Chessboard.getRow(fromSquare, this.activeColor) == 2 && Chessboard.getRow(toSquare, this.activeColor) == 4) {
            this.enpassantSquare = Chessboard.getSquare(Chessboard.getSquareLetter(fromSquare), this.activeColor == Color.WHITE ? '3' : '6');
        }
        if (isCapture || pieceType == Piece.PAWN) {
            this.plyCount = 0;
        } else {
            this.plyCount++;
        }
        this.activeColor = Color.getEnemyColor(this.activeColor);
        if (this.activeColor == Color.WHITE) {
            this.moveNumber++;
        }
    }

    /**  */
    public final Piece getPiece(int square) {
        return this.simplePosition.getPiece(square);
    }

    /**  */
    public final int getActiveColor() {
        return this.activeColor;
    }

    /**  */
    public final boolean isAvailableCastle(int color, int castleType) {
        return this.isAvailableCastle[color][castleType];
    }

    /**  */
    public final int getEnpassantSquare() {
        return this.enpassantSquare;
    }

    /**  */
    public final int getPlyCount() {
        return this.plyCount;
    }

    /**  */
    public final int getMoveNumber() {
        return this.moveNumber;
    }

    /**  */
    public String getFen() {
        StringBuilder builder = new StringBuilder();
        int emptySquare = 0;
        for (int i = 0; i < 64; i++) {
            if (i % 8 == 0 && i > 0) {
                if (emptySquare > 0) {
                    builder.append(emptySquare);
                    emptySquare = 0;
                }
                builder.append('/');
            }
            Piece piece = this.simplePosition.getPiece((7 - i / 8) * 8 + i % 8);
            if (piece != null) {
                if (emptySquare > 0) {
                    builder.append(emptySquare);
                    emptySquare = 0;
                }
                builder.append(piece);
            } else {
                emptySquare++;
            }
        }
        if (emptySquare > 0) {
            builder.append(emptySquare);
        }
        builder.append(' ');
        switch(this.activeColor) {
            case Color.WHITE:
                builder.append('w');
                break;
            case Color.BLACK:
                builder.append('b');
                break;
            default:
                throw new IllegalArgumentException();
        }
        builder.append(' ');
        boolean existOO = false;
        if (this.isAvailableCastle[Color.WHITE][Castle.SHORT_CASTLE]) {
            builder.append('K');
            existOO = true;
        }
        if (this.isAvailableCastle[Color.WHITE][Castle.LONG_CASTLE]) {
            builder.append('Q');
            existOO = true;
        }
        if (this.isAvailableCastle[Color.BLACK][Castle.SHORT_CASTLE]) {
            builder.append('k');
            existOO = true;
        }
        if (this.isAvailableCastle[Color.BLACK][Castle.LONG_CASTLE]) {
            builder.append('q');
            existOO = true;
        }
        if (!existOO) {
            builder.append('-');
        }
        builder.append(' ');
        if (this.enpassantSquare == Chessboard.SQUARE_NONE) {
            builder.append('-');
        } else {
            builder.append(Character.toLowerCase(Chessboard.getSquareLetter(this.enpassantSquare)));
            builder.append(Chessboard.getSquareNumber(this.enpassantSquare));
        }
        builder.append(' ');
        builder.append(this.plyCount);
        builder.append(' ');
        builder.append(this.moveNumber);
        return builder.toString();
    }

    /**  */
    public String toString() {
        StringBuilder[] builders = Chessboard.getPositionStringBuilders(this.simplePosition);
        if (this.activeColor == Color.WHITE) {
            builders[17].append("  WHITE");
        } else {
            builders[0].append("  BLACK");
        }
        if (this.isAvailableCastle[Color.WHITE][Castle.SHORT_CASTLE]) {
            builders[14].append("  OO");
        }
        if (this.isAvailableCastle[Color.WHITE][Castle.LONG_CASTLE]) {
            builders[15].append("  OOO");
        }
        if (this.isAvailableCastle[Color.BLACK][Castle.SHORT_CASTLE]) {
            builders[2].append("  OO");
        }
        if (this.isAvailableCastle[Color.BLACK][Castle.LONG_CASTLE]) {
            builders[3].append("  OOO");
        }
        builders[8].append("  ply  = ").append(this.plyCount);
        builders[9].append("  move = ").append(this.moveNumber);
        if (this.enpassantSquare != Chessboard.SQUARE_NONE) {
            builders[11].append("  e.p. = ").append(Chessboard.getSquareString(this.enpassantSquare));
        }
        return Chessboard.getStringFromStringBuilders(builders);
    }
}
