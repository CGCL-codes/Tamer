package net.sourceforge.plantuml.sequencediagram.command;

import net.sourceforge.plantuml.sequencediagram.MessageExoType;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;

public class CommandExoArrowLeft extends CommandExoArrowAny {

    public CommandExoArrowLeft(SequenceDiagram sequenceDiagram) {
        super(sequenceDiagram, "(?i)^(\\[?[=-]+(?:>>?|//?|\\\\\\\\?)|\\[?(?:<<?|//?|\\\\\\\\?)[=-]+)\\s*([\\p{L}0-9_.@]+|\"[^\"]+\")\\s*(?::\\s*(.*))?$", 0, 1);
    }

    @Override
    MessageExoType getMessageExoType(String arrow) {
        if (arrow.contains(">")) {
            return MessageExoType.FROM_LEFT;
        }
        if (arrow.contains("<")) {
            return MessageExoType.TO_LEFT;
        }
        if (arrow.startsWith("/") || arrow.startsWith("[/") || arrow.startsWith("\\") || arrow.startsWith("[\\")) {
            return MessageExoType.TO_LEFT;
        }
        if (arrow.endsWith("\\") || arrow.endsWith("/")) {
            return MessageExoType.FROM_LEFT;
        }
        throw new IllegalArgumentException(arrow);
    }
}
