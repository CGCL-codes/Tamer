package vidis.modules.pingPong;

import vidis.data.AUserPacket;
import vidis.data.annotation.ColorType;
import vidis.data.annotation.ComponentColor;
import vidis.data.annotation.ComponentInfo;

@ComponentInfo(name = "Ping")
@ComponentColor(color = ColorType.GREEN)
public class Ping extends AUserPacket {
}
