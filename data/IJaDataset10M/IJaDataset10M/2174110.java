/*
 * $Source: /cvsroot/freegle/freegle/src/drjava/freegle/tests/experimental/DBMock.java,v $
 *
 * $Log: DBMock.java,v $
 * Revision 1.1  2001/06/25 19:47:01  drjava
 * restructuring a few things... not a pretty sight in the meantime
 *
 */

package drjava.freegle.tests;

import java.sql.SQLException;
import java.util.*;
import drjava.util.*;
import drjava.freegle.*;

/**
 * a simple implementation of DBInterface used by unit tests
 *
 * @version $Revision: 1.1 $
 * @author $Author: drjava $
 */

public class DBMock extends Mock implements DBInterface {
  Vector/*<FUri>*/ uris = new Vector();
  Hashtable/*<Integer, FDocument>*/ docs = new Hashtable();
  int idCounter;
  
  public boolean isTestDB() {
    return true;
  }
  
  public void deleteEverything() {
    uris.clear();
  }
  
  public void close() throws SQLException {
  }
  
  public void chaseRedirectsUpward(Vector/*<FUri>*/ uris) throws ChaseException, SQLException  {
    throw unimplemented();
  }
  
  public FDocument loadDocument(int id) throws SQLException {
    throw unimplemented();
  }
  
  public FUri loadFUri(String uri) throws SQLException {
    for (int i = 0; i < uris.size(); i++) {
      FUri u = (FUri) uris.get(i);
      if (u.getURI().equals(uri))
        return u;
    }
    return null;
  }
  
  public Enumeration/*<Integer>*/ allDocumentIds() throws SQLException  {
    return docs.keys();
  }
  
  public Vector/*<FUri>*/ urisForDocument(int docId) throws SQLException  {
    Vector/*<FUri>*/ v = new Vector();
    for (int i = 0; i < uris.size(); i++) {
      FUri uri = (FUri) uris.get(i);
      if (uri.g
  }
  
  /** @returns true for non-dbrs! */  
  public boolean isLastVerifiedEdition(FUri uri) throws SQLException  {
    throw unimplemented();
  }
  
  public int addDocument(FDocument doc) {
    int id = ++idCounter;
    docs.put(new Integer(id), doc);
    return id;  
  }
  
  public int addUri(String uri, FDocument doc) {
    int docId = addDocument(doc);
    int uriId = ++idCounter;
    uris.add(new FUri(uriId, uri));
    return uriId;
  }
}
