package com.adobe.dp.epub.io;

import java.io.IOException;
import java.io.InputStream;

public abstract class DataSource {

    public abstract InputStream getInputStream() throws IOException;
}
