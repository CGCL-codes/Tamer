package org.jowidgets.spi.impl.mask;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jowidgets.common.mask.ICharacterMask;
import org.jowidgets.common.mask.ITextMask;
import org.jowidgets.common.mask.TextMaskMode;
import org.jowidgets.util.Assert;

final class TextMask implements ITextMask {

    private final ArrayList<ICharacterMask> characterMasks;

    private final TextMaskMode mode;

    TextMask(final List<? extends ICharacterMask> characterMasks, final TextMaskMode mode) {
        Assert.paramNotNull(characterMasks, "characterMasks");
        Assert.paramNotNull(mode, "mode");
        this.characterMasks = new ArrayList<ICharacterMask>(characterMasks);
        this.mode = mode;
    }

    @Override
    public int getLength() {
        return characterMasks.size();
    }

    @Override
    public ICharacterMask getCharacterMask(final int index) {
        return characterMasks.get(index);
    }

    @Override
    public TextMaskMode getMode() {
        return mode;
    }

    @Override
    public String getPlaceholder() {
        final StringBuilder result = new StringBuilder();
        for (int i = 0; i < getLength(); i++) {
            final ICharacterMask mask = getCharacterMask(i);
            if (mask.getPlaceholder() != null) {
                result.append(mask.getPlaceholder().charValue());
            }
        }
        return result.toString();
    }

    @Override
    public Iterator<ICharacterMask> iterator() {
        return characterMasks.iterator();
    }
}
