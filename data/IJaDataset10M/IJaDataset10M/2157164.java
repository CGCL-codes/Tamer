package bestidioms;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;

public abstract class ForEachLine {

    public ForEachLine(File file, final String encoding) {
        try {
            new ForResource<FileInputStream>(new FileInputStream(file)) {

                public void work(FileInputStream inputStream) throws IOException {
                    new ForResource<InputStreamReader>(new InputStreamReader(inputStream, encoding)) {

                        public void work(InputStreamReader inputStreamReader) {
                            new ForResource<BufferedReader>(new BufferedReader(inputStreamReader)) {

                                public void work(BufferedReader reader) throws IOException {
                                    String line;
                                    while ((line = reader.readLine()) != null) ForEachLine.this.work(line);
                                }
                            };
                        }
                    };
                }
            };
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract void work(String line);
}
