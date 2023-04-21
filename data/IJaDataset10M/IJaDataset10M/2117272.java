package rbnb;

import imageTools.AnnotatedImage;
import imageTools.ImageBank;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import com.rbnb.sapi.ChannelMap;
import com.rbnb.sapi.SAPIException;
import com.rbnb.sapi.Source;

public class Util {

    public static void loadBankIntoRBNB(ImageBank<AnnotatedImage> bank, int cacheSize, int archiveSize, String rbnbHostName, String srcName) throws IOException, SAPIException {
        Source source = new Source(cacheSize, "append", archiveSize);
        source.OpenRBNBConnection(rbnbHostName, srcName);
        ChannelMap cmap = new ChannelMap();
        int ch_Img = cmap.Add("Image");
        cmap.PutMime(ch_Img, "image/jpeg");
        for (AnnotatedImage img : bank) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(img, "jpg", out);
            double[] times = { img.getTimestamp() };
            cmap.PutTimes(times);
            cmap.PutDataAsByteArray(ch_Img, out.toByteArray());
            System.out.println("Put " + img.getName() + " into " + rbnbHostName);
            source.Flush(cmap, true);
        }
        source.Detach();
        source.CloseRBNBConnection();
    }
}
