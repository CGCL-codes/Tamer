package mandelbrot.components;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.imageio.ImageIO;
import org.apache.log4j.Logger;
import org.procol.framework.components.AbstractComponent;
import org.procol.framework.components.exceptions.ComponentInvocationException;
import lights.exceptions.TupleSpaceException;
import lights.interfaces.ITuple;
import lights.interfaces.ITupleSpace;
import lights.space.Field;
import lights.space.Tuple;
import lights.utils.ObjectTuple;
import mandelbrot.dataobjects.Complex;
import mandelbrot.dataobjects.Configuration;
import mandelbrot.dataobjects.Image;
import mandelbrot.gui.SimpleMandelbrotGui;

public class Presenter extends AbstractComponent {

    private Configuration config;

    private List<Image> images = new ArrayList<Image>();

    private int[][] mandelbrotMatrix;

    public Presenter(HashMap<String, ITupleSpace> spaces) {
        super(spaces);
    }

    @Override
    public Boolean call() throws ComponentInvocationException {
        ITuple configTemplate = new Tuple().add(new Field().setType(Integer.class)).add(new Field().setType(Integer.class)).add(new Field().setType(Integer.class)).add(new Field().setType(Complex.class)).add(new Field().setType(Complex.class)).add(new Field().setType(Boolean.class));
        ObjectTuple objtuple = null;
        try {
            objtuple = (ObjectTuple) spaces.get("Space").rd(configTemplate);
        } catch (TupleSpaceException e1) {
            e1.printStackTrace();
        }
        this.config = (Configuration) objtuple.getObject();
        ITuple template = new Tuple().add(new Field().setType(Integer.class)).add(new Field().setType(Complex.class)).add(new Field().setType(Complex.class)).add(new Field().setType(Integer[][].class)).add(new Field().setValue(true));
        ITuple[] resultTuples = new Tuple[config.getNumberOfImagePartitions()];
        try {
            for (int i = 0; i < config.getNumberOfImagePartitions(); i++) {
                resultTuples[i] = spaces.get("Space").in(template);
            }
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
        for (ITuple t : resultTuples) {
            ObjectTuple objectTuple = (ObjectTuple) t;
            this.images.add((Image) objectTuple.getObject());
        }
        computeMatrix();
        saveMandelbrotImageAsFile();
        if (config.getIsBooleanPresented() == true) {
            printGui();
        }
        return true;
    }

    private void printGui() {
        new SimpleMandelbrotGui(mandelbrotMatrix);
    }

    private void computeMatrix() {
        this.mandelbrotMatrix = new int[config.getHeight()][config.getWidth()];
        int partitionHeight = config.getHeight() / config.getNumberOfImagePartitions();
        for (Image image : images) {
            for (int row = 0; row < config.getHeight() / config.getNumberOfImagePartitions(); row++) {
                for (int col = 0; col < config.getWidth(); col++) {
                    this.mandelbrotMatrix[(image.getId() * partitionHeight) + row][col] = image.getLevel(row, col);
                }
            }
        }
    }

    private void saveMandelbrotImageAsFile() {
        BufferedImage bufferedImage = new BufferedImage(config.getWidth(), config.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = bufferedImage.createGraphics();
        for (int row = 0; row < config.getHeight(); row++) {
            for (int col = 0; col < config.getWidth(); col++) {
                int level = mandelbrotMatrix[row][col];
                if (level == 0) {
                    graphics.setColor(Color.black);
                } else {
                    graphics.setColor(new Color(level * 2, 0, 256 - level * 2));
                }
                graphics.drawRect(col, row, 0, 0);
            }
        }
        graphics.dispose();
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HHmm-ss-SSSZ");
        StringBuilder dateStr = new StringBuilder(dateFormat.format(currentDate));
        File file = new File("mandelbrot_" + dateStr + ".png");
        try {
            ImageIO.write(bufferedImage, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
