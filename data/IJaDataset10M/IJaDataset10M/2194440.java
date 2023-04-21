package com.semp.gu.codec;

import java.io.IOException;
import com.semp.gu.domain.local.LocalAlbumElement;

public interface ICodec {

    public void processElement(LocalAlbumElement element) throws IOException;
}
