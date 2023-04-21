package cham.open.pattern.decorator;

import java.util.logging.Logger;

/**
 *
 * @author Chaminda Amarasinghe <chaminda.amarasinghe@headstrong.com>
 */
public class SplitDecorator extends TextDecorator {

    private final Logger logger = Logger.getLogger(SplitDecorator.class.getName());

    public SplitDecorator(TextComponent child) {
        super(child);
    }

    @Override
    protected String before(String plain) {
        logger.info("Before Decorating on SplitDecorator " + plain);
        return plain;
    }

    @Override
    protected String after(String plain) {
        logger.info("After Decorating on SplitDecorator " + plain);
        return plain.replace(" ", ", ");
    }
}
