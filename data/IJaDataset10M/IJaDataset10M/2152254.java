package jacky.lanlan.song.extension.struts.action;

import java.util.List;
import org.apache.struts.upload.FormFile;
import jacky.lanlan.song.closure.Factory;
import jacky.lanlan.song.extension.struts.resource.ResourcePersistence;
import jacky.lanlan.song.util.Assert;

public class TestPersistenceFactory implements Factory<ResourcePersistence> {

    public ResourcePersistence create() {
        return new ResourcePersistence() {

            public void persistence(List<FormFile> uploadFiles) {
                Assert.isTrue(uploadFiles.size() == 2, "应该有2个Mock FormFile");
            }
        };
    }
}
