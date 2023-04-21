package yaw.uml.ea;

import org.sparx.Repository;

public class EARepositoryCache {

    private static String SEARCH_XML = "<?xml version=\"1.0\" encoding=\"windows-1252\"?>\n\n<RootSearch><Search Name=\"Element\" GUID=\"{4A34D85D-EED3-4e25-BFC3-85B5855FB641}\" PkgGUID=\"-1\" Type=\"0\" LnksToObj=\"0\" CustomSearch=\"0\" AddinAndMethodName=\"\"><SrchOn><RootTable Type=\"0\"><TableName Display=\"Element\" Name=\"t_object\"/><TableHierarchy Display=\"\" Hierarchy=\"t_object\"/><Field Filter=\"t_object.Name = '&lt;Search Term&gt;'\" Text=\"&lt;Search Term&gt;\" IsDateField=\"0\" Type=\"1\" Required=\"1\" Active=\"1\"><TableName Display=\"Element\" Name=\"t_object\"/><TableHierarchy Display=\"Element\" Hierarchy=\"t_object\"/><Condition Display=\"Equal To\" Type=\"=\"/><FieldName Display=\"Name\" Name=\"t_object.Name\"/></Field></RootTable></SrchOn><LnksTo/></Search></RootSearch>";

    private String filename;

    private Repository repository;

    public EARepositoryCache() {
    }

    public Repository open(String filename) {
        if (this.filename != null && this.filename.equals(filename)) return repository;
        close();
        this.repository = new Repository();
        repository.SetSuppressEADialogs(true);
        repository.SetEnableUIUpdates(false);
        repository.SetEnableCache(true);
        repository.SetBatchAppend(true);
        if (repository.OpenFile(filename)) {
            repository.AddDefinedSearches(SEARCH_XML);
            this.filename = filename;
            return repository;
        } else close();
        return null;
    }

    private void close() {
        if (repository != null) {
            repository.CloseFile();
            repository.destroy();
            repository = null;
            filename = null;
        }
    }
}
