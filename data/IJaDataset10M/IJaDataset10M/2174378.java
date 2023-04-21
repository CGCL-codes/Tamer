package net.sf.securejdms.server.document.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.sql.Timestamp;
import net.sf.securejdms.server.document.Folder;
import org.junit.Test;

public class FolderTest extends AbstractDocumentTest {

    @Test
    public void testRootAndSubFolders() {
        String folder1Name = "folder1 " + random.nextInt();
        String folder2Name = "folder2 " + random.nextInt();
        String folder3Name = "folder3 " + random.nextInt();
        em.getTransaction().begin();
        Folder folder1 = new Folder();
        folder1.setName(folder1Name);
        folder1.setCreationTimestamp(new Timestamp(System.currentTimeMillis()));
        em.persist(folder1);
        Folder folder2 = new Folder();
        folder2.setName(folder2Name);
        folder2.setDescription("Descr1");
        folder2.setCreationTimestamp(new Timestamp(System.currentTimeMillis()));
        em.persist(folder2);
        Folder folder3 = new Folder();
        folder3.setName(folder3Name);
        folder3.setCreationTimestamp(new Timestamp(System.currentTimeMillis()));
        em.persist(folder3);
        folder1.addToChildren(folder2);
        folder1.addToChildren(folder3);
        em.getTransaction().commit();
        em.clear();
        folder1 = em.find(Folder.class, folder1.getId());
        assertNotNull(folder1);
        assertEquals(folder1Name, folder1.getName());
        assertEquals(2, folder1.getChildren().size());
        int id2 = folder1.getChildren().get(0).equals(folder2) ? 0 : 1;
        int id3 = id2 == 0 ? 1 : 0;
        assertEquals(folder2Name, folder1.getChildren().get(id2).getName());
        assertEquals(folder1, folder1.getChildren().get(id2).getParentFolder());
        assertEquals(folder1Name, folder1.getChildren().get(id2).getParentFolder().getName());
        assertEquals(folder3, folder1.getChildren().get(id3));
        assertEquals(folder3Name, folder1.getChildren().get(id3).getName());
        assertEquals(folder1, folder1.getChildren().get(id3).getParentFolder());
        assertEquals(folder1Name, folder1.getChildren().get(id3).getParentFolder().getName());
    }
}
