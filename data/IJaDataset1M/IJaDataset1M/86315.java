package com.document.tag.impl;

import com.document.tag.api.TagService;
import com.document.tag.api.Tag;
import com.document.tag.api.TagContent;
import com.document.tag.api.Wrapper;
import com.document.tag.api.encode.AbstractEncoderService;
import com.document.tag.api.image.ImageUtils;
import com.document.tag.configuration.generated.TagConfiguration;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * <Description>.
 *
 * @author Manuel Martins
 */
public class TagServiceImpl<W extends Wrapper, C extends TagContent, T extends Tag<C>> implements TagService<W, C, T> {

    private final AbstractEncoderService abstractEncoderService = new EncoderServiceImpl();

    private final ImageUtils imageUtils = new ImageUtils();

    @Override
    public W tag(final InputStream inputStream, final Tag<?> tag, final TagConfiguration configuration) throws WriterException, IOException {
        final BitMatrix matrix = this.abstractEncoderService.encode(tag.getTagContent(), configuration);
        return null;
    }

    @Override
    public W tag(final File file, final Tag<?> tag, final TagConfiguration configuration) throws WriterException, IOException {
        final BitMatrix matrix = this.abstractEncoderService.encode(tag.getTagContent(), configuration);
        return null;
    }

    @Override
    public void tag(final InputStream inputStream, final OutputStream outputStream, final Tag<?> tag, final TagConfiguration configuration) throws WriterException, IOException {
        final BitMatrix matrix = this.abstractEncoderService.encode(tag.getTagContent(), configuration);
    }

    @Override
    public void tag(final File fileIn, final File fileOut, final Tag<?> tag, final TagConfiguration configuration) throws WriterException, IOException, FileNotFoundException {
        final BitMatrix matrix = this.abstractEncoderService.encode(tag.getTagContent(), configuration);
    }

    @Override
    public W read(final InputStream image) {
        return null;
    }

    @Override
    public W read(final File fileIn) {
        return null;
    }

    @Override
    public W read(final T image) {
        return null;
    }
}
