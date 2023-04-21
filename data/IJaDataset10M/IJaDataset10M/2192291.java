package io;

import client.view.sprite.Animation;
import static common.constant.ErrConstant.ERR_INPUT_WRONG_NUM_ARGS;
import static common.constant.ErrConstant.ERR_READ_LINE;
import io.image.ImageLib;
import org.apache.log4j.Logger;
import tools.MatrixUtil;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * @author stefan
 * @since 2.0
 */
public class AnimConfigParser {

    private static final Logger logger = Logger.getLogger(AnimConfigParser.class);

    private static final String COMMENT_PREFIX = "//";

    private static final Character STRIP_ANIM_SYMBOL = 's';

    private static final Character MATRIX_ANIM_SYMBOL = 'm';

    private ImageLib imageLib;

    private AnimLib animLib;

    public AnimConfigParser(ImageLib imageLib, AnimLib animLib) {
        this.imageLib = imageLib;
        this.animLib = animLib;
    }

    /**
   * @param stream the config file stream
   * @throws java.io.IOException when an image could not be loaded
   */
    public void loadConfigFile(InputStream stream) throws IOException {
        String line;
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        try {
            while ((line = br.readLine()) != null) {
                if (line.length() == 0) continue;
                if (line.startsWith(COMMENT_PREFIX)) continue;
                parseCmd(line);
            }
        } finally {
            br.close();
        }
    }

    public void parseCmd(String line) {
        if (line == null) {
            throw new IllegalArgumentException("Line is null");
        }
        char ch = Character.toLowerCase(line.charAt(0));
        if (ch == STRIP_ANIM_SYMBOL) {
            getStripAnim(line);
        } else if (ch == MATRIX_ANIM_SYMBOL) {
            getStripInsideMatrixAnim(line);
        } else throw new IllegalArgumentException(ERR_READ_LINE + ": " + line + ", unknown Char: " + ch);
    }

    /**
   * format:
   * S AnimName imgName frameDuration startFrame totalframecount loopForever
   */
    private void getStripAnim(String line) throws NumberFormatException {
        StringTokenizer tokens = new StringTokenizer(line);
        Scanner cmdScanner = new Scanner(line);
        if (!(tokens.countTokens() > 6)) throw new IllegalArgumentException(ERR_INPUT_WRONG_NUM_ARGS + " for " + line + " Usage: S AnimName imgName frameDuration startFrame totalframecount loopForever"); else {
            cmdScanner.next();
            String animName = cmdScanner.next();
            String imgName = cmdScanner.next();
            int frameDuration = cmdScanner.nextInt();
            int firstFrameCol = cmdScanner.nextInt();
            int lastFrameCol = cmdScanner.nextInt();
            boolean loop = cmdScanner.nextBoolean();
            List<BufferedImage> result = new ArrayList<BufferedImage>();
            List<BufferedImage> imgRow = imageLib.getImages(imgName);
            for (int col = firstFrameCol; col < lastFrameCol && col < imgRow.size(); col++) {
                result.add(imgRow.get(col));
            }
            Animation anim = animLib.createAnim(result, frameDuration, 0, result.size(), loop);
            animLib.addAnim(animName, anim);
        }
    }

    /**
   * format:
   * M AnimName imgName frameDuration startFrame totalframecount loopForever
   */
    private void getStripInsideMatrixAnim(String line) throws NumberFormatException {
        StringTokenizer tokens = new StringTokenizer(line);
        Scanner cmdScanner = new Scanner(line);
        if (!(tokens.countTokens() == 9)) throw new IllegalArgumentException(ERR_INPUT_WRONG_NUM_ARGS + " for " + line + " Usage: M AnimName imgName firstFrameCol firstFrameRow lastFrameCol lastFrameRow loopForever"); else {
            cmdScanner.next();
            String animName = cmdScanner.next();
            String imgName = cmdScanner.next();
            int frameDuration = cmdScanner.nextInt();
            int firstFrameCol = cmdScanner.nextInt();
            int firstFrameRow = cmdScanner.nextInt();
            int lastFrameCol = cmdScanner.nextInt();
            int lastFrameRow = cmdScanner.nextInt();
            boolean loop = cmdScanner.nextBoolean();
            Point startFrame = new Point(firstFrameCol, firstFrameRow);
            Point endFrame = new Point(lastFrameCol, lastFrameRow);
            List<List<BufferedImage>> sheet = imageLib.getAnimImages(imgName);
            List<BufferedImage> result = MatrixUtil.grabInnerList(sheet, startFrame, endFrame);
            Animation anim = animLib.createAnim(result, frameDuration, 0, result.size(), loop);
            animLib.addAnim(animName, anim);
        }
    }
}
