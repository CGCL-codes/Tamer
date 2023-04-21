package layout;

import javax.swing.JFrame;
import com.googlecode.harapeko.Harapeko;
import com.googlecode.harapeko.annotation.JFrameResource;
import com.googlecode.harapeko.annotation.layout.FlowLayout;

@FlowLayout(layout = { "component1", "component2", "component3", "component4", "component5" })
@JFrameResource(defaultCloseOperation = JFrame.EXIT_ON_CLOSE)
public class FlowLayoutExample extends JFrame {

    public static void main(String[] args) {
        Harapeko.launch(FlowLayoutExample.class);
    }
}
