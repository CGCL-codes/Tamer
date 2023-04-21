package marcin.downloader.model.interpreters;

import marcin.downloader.model.data.CaptchaData;
import marcin.downloader.model.data.LinkData;
import marcin.downloader.model.exceptions.MessageInterpretException;
import marcin.downloader.model.utils.Message;

public class CaptchaInterpreter extends TypeLinkInterpreter {

    private LinkInterpreter captchaLink;

    public CaptchaInterpreter(TypeInterpreter ti, LinkInterpreter li, LinkInterpreter captchaLink) {
        super(ti, li);
        this.captchaLink = captchaLink;
    }

    @Override
    public boolean checkMessage(Message m) throws MessageInterpretException {
        if (!super.checkMessage(m)) return false;
        return captchaLink.checkMessage(m);
    }

    @Override
    public LinkData getData() {
        return new CaptchaData((LinkData) super.getData(), (LinkData) captchaLink.getData());
    }

    @Override
    public void reset() {
        super.reset();
        captchaLink.reset();
    }
}
