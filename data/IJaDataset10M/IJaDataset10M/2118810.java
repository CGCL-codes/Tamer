package common.webapp.servlet;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import common.context.CacheHelper;
import common.povo.Constant;

/**
 * Class <code>VerifyCodeServlet</code> is VerifyCodeServlet
 * 
 * @author <a href="mailto:zmuwang@gmail.com">muwang zheng</a>
 * @version 1.0, 2011-8-26
 */
public class VerifyCodeServlet extends HttpServlet {

    /**
	 * serialVersionUID
	 */
    private static final long serialVersionUID = 2389977211371805346L;

    /**
	 * log
	 */
    protected static final Log LOG = LogFactory.getLog(VerifyCodeServlet.class);

    private int width = 60;

    private int height = 20;

    private int codeCount = 4;

    private int x = 0;

    private int startx = 0;

    private int fontHeight;

    private int codeY;

    char[] codeSequence = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

    /**
	 * 初始化验证图片属性
	 */
    public void init() throws ServletException {
        String strWidth = this.getInitParameter("width");
        String strHeight = this.getInitParameter("height");
        String strCodeCount = this.getInitParameter("codeCount");
        try {
            if (strWidth != null && strWidth.length() != 0) {
                width = Integer.parseInt(strWidth);
            }
            if (strHeight != null && strHeight.length() != 0) {
                height = Integer.parseInt(strHeight);
            }
            if (strCodeCount != null && strCodeCount.length() != 0) {
                codeCount = Integer.parseInt(strCodeCount);
            }
        } catch (NumberFormatException e) {
        }
        x = width / (codeCount + 1);
        startx = x / 2;
        fontHeight = height - 2;
        codeY = height - 4;
    }

    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, java.io.IOException {
        try {
            BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = buffImg.createGraphics();
            Random random = new Random();
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, width, height);
            Font font = new Font("Fixedsys", Font.PLAIN, fontHeight);
            g.setFont(font);
            g.setColor(Color.BLACK);
            g.drawRect(0, 0, width - 1, height - 1);
            int lineNum = 5;
            int lineColor = random.nextInt(3);
            g.setColor(lineColor == 0 ? Color.RED : lineColor == 1 ? Color.GREEN : Color.BLUE);
            for (int i = 0; i < lineNum; i++) {
                int x = random.nextInt(width);
                int y = random.nextInt(height);
                int xl = random.nextInt(12);
                int yl = random.nextInt(12);
                g.drawLine(x, y, x + xl, y + yl);
            }
            StringBuffer randomCode = new StringBuffer();
            int red = 0, green = 0, blue = 0;
            try {
                int pos = 0;
                char bc = 0;
                for (int i = 0; i < codeCount; i++) {
                    pos = random.nextInt(36);
                    bc = codeSequence[pos];
                    String strRand = String.valueOf(bc);
                    red = random.nextInt(255);
                    green = random.nextInt(255);
                    blue = random.nextInt(255);
                    g.setColor(new Color(red, green, blue));
                    g.drawString(strRand, i * x + startx, codeY);
                    randomCode.append(strRand);
                }
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
            CacheHelper.setUserData(Constant.USER_KEY_VALIDATECODE, randomCode.toString());
            resp.setHeader("Pragma", "no-cache");
            resp.setHeader("Cache-Control", "no-cache");
            resp.setDateHeader("Expires", 0);
            resp.setContentType("image/jpeg");
            ServletOutputStream os = resp.getOutputStream();
            ImageIO.write(buffImg, "jpeg", os);
            os.close();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.service(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.service(req, resp);
    }

    public static void main(String[] args) {
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            System.out.println(random.nextInt(2));
        }
    }
}
