package wing.message;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import wing.Block;
import wing.WingMove;

public class MoveMessage extends Message {

    private static final Pattern p0 = Pattern.compile("Game (\\d+) I: (\\S+) \\(([ 0-9\\-]+)\\) vs (\\S+) \\(([ 0-9\\-]+)\\).*");

    private static final Pattern p1 = Pattern.compile("\\s*([-\\d]+)\\((.)\\): (.*)");

    private int gameNo;

    private String bName;

    private int bHama;

    private int bSec;

    private int bMove;

    private String wName;

    private int wHama;

    private int wSec;

    private int wMove;

    private ArrayList<WingMove> moveList;

    public MoveMessage(Block block) {
        super(block);
        String firstLine = block.get(0);
        Matcher m0 = p0.matcher(firstLine);
        if (m0.matches()) {
            gameNo = Integer.parseInt(m0.group(1));
            wName = m0.group(2);
            String wInfo = m0.group(3);
            bName = m0.group(4);
            String bInfo = m0.group(5);
            String[] wi = wInfo.split("\\s");
            String[] bi = bInfo.split("\\s");
            wHama = Integer.parseInt(wi[0]);
            wSec = Integer.parseInt(wi[1]);
            wMove = Integer.parseInt(wi[2]);
            bHama = Integer.parseInt(bi[0]);
            bSec = Integer.parseInt(bi[1]);
            bMove = Integer.parseInt(bi[2]);
            moveList = new ArrayList<WingMove>();
            for (String s : block.getMessageCollection()) {
                if (s.equals(firstLine)) {
                    continue;
                }
                WingMove wingMove = new WingMove(s);
                moveList.add(wingMove);
            }
        } else {
            System.err.println("MoveMessage parse:" + firstLine);
        }
        System.out.println("MoveMessage:" + this.toString());
    }

    public int getGameNo() {
        return gameNo;
    }

    public String getBlackName() {
        return bName;
    }

    public int getBlackSec() {
        return bSec;
    }

    public int getBlackMove() {
        return bMove;
    }

    public String getWhiteName() {
        return wName;
    }

    public int getWhiteSec() {
        return wSec;
    }

    public int getWhiteMove() {
        return wMove;
    }

    public ArrayList<WingMove> getWingMoveList() {
        return moveList;
    }
}
