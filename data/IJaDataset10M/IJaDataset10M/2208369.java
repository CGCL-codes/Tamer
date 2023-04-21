package marubinotto.piggydb.model.files;

import java.util.List;
import marubinotto.piggydb.external.file.FileRepositoryImpl;
import marubinotto.piggydb.fixture.mock.FileItemMock;
import marubinotto.piggydb.model.FileRepository;
import marubinotto.piggydb.model.RepositoryTestBase;
import marubinotto.piggydb.model.entity.RawFragment;
import marubinotto.piggydb.model.repository.FileRepositoryRI;
import marubinotto.util.FileSystemUtils;
import org.junit.runners.Parameterized.Parameters;

public abstract class FileRepositoryTestBase extends RepositoryTestBase<FileRepository> {

    public FileRepositoryTestBase(RepositoryFactory<FileRepository> factory) {
        super(factory);
    }

    @Parameters
    public static List<Object[]> factories() {
        return toParameters(new RepositoryFactory<FileRepository>() {

            public FileRepository create() throws Exception {
                return new FileRepositoryRI();
            }
        }, new RepositoryFactory<FileRepository>() {

            public FileRepository create() throws Exception {
                FileRepositoryImpl repository = new FileRepositoryImpl();
                repository.setBaseDirectory(FileSystemUtils.getEmptyDirectory());
                return repository;
            }
        }, new RepositoryFactory<FileRepository>() {

            public FileRepository create() throws Exception {
                FileRepositoryImpl repository = new FileRepositoryImpl();
                repository.setDatabasePath("mem:piggydb");
                return repository;
            }
        });
    }

    protected void registerFile(long id, String fileName, String content) throws Exception {
        RawFragment fragment = new RawFragment();
        fragment.setId(id);
        fragment.setFileInput(new FileItemMock("file", fileName, content.getBytes()));
        this.object.putFile(fragment);
    }
}
