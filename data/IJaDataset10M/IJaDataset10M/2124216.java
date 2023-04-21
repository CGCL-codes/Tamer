package Institute;

/**
 * Cache' Java Class Generated for class Institute.Speciality on version Cache for Windows (x86-32) 2010.1.4 (Build 803) Tue Aug 24 2010 00:04:40 EDT.<br>
 *
 * @see <a href = "http://cache-server:57772/csp/documatic/%25CSP.Documatic.cls?APP=1&PAGE=CLASS&LIBRARY=TPCS&CLASSNAME=Institute.Speciality</A>
**/
public class Speciality extends com.intersys.classes.Persistent {

    private static final long serialVersionUID = 7285;

    private static String CACHE_CLASS_NAME = "Institute.Speciality";

    /**
           <p>NB: DO NOT USE IN APPLICATION(!!!).
           <br>Use <code>Institute.Speciality.open</code> instead!</br></p>
           <p>
           Used to construct a Java object, corresponding to existing object
           in Cache database.
           </p>
           @see #_open(com.intersys.objects.Database, com.intersys.objects.Oid)
           @see #open(com.intersys.objects.Database, com.intersys.objects.Oid)
    */
    public Speciality(com.intersys.cache.CacheObject ref) throws com.intersys.objects.CacheException {
        super(ref);
    }

    public Speciality(com.intersys.objects.Database db, String initstr) throws com.intersys.objects.CacheException {
        super(((com.intersys.cache.SysDatabase) db).newCacheObject(CACHE_CLASS_NAME, initstr));
    }

    /**
       Creates a new instance of object "Institute.Speciality" in Cache
       database and corresponding object of class
       <code>Institute.Speciality</code>.

       @param db <code>Database</code> object used for connection with
       Cache database.

       @throws com.intersys.objects.CacheException in case of error.

              @see #_open(com.intersys.objects.Database, com.intersys.objects.Oid)
              @see #open(com.intersys.objects.Database, com.intersys.objects.Oid)
     */
    public Speciality(com.intersys.objects.Database db) throws com.intersys.objects.CacheException {
        super(((com.intersys.cache.SysDatabase) db).newCacheObject(CACHE_CLASS_NAME));
    }

    /**
    * Calls method
    * {@link com.intersys.objects.Database#createObjects(String, java.util.Collection)}
    * to save a collection of previously created new objects in Cache
    * Database. No object in collection could been saved before. All
    * objects must be instances of <code>Institute.Speciality</code> and not of its
    * subclasses. To save changes in objects that already exist in
    * the database use <code>saveObjects</code> method or instance
    * method <code>save</code> on the object itself.
    *
    * @param db <code>Database</code> object used for connection with
    * Cache database.
    *
    * @param objects A collection of newly created objects. None of
    * the objects in the collection may already exist in database.
    *
    * @see #saveObjects(com.intersys.objects.Database, java.util.Collection)
    * @see com.intersys.objects.Database#createObjects(String, java.util.Collection)
    *
    */
    public static void createObjects(com.intersys.objects.Database db, java.util.Collection objects) throws com.intersys.objects.CacheException {
        db.createObjects(CACHE_CLASS_NAME, objects);
    }

    /**
       Runs method <code> %OpenId </code> in Cache to open an object
       from Cache database and creates corresponding object of class
       <code>Institute.Speciality</code>.

       @return <code> RegisteredObject </code>, corresponding to opened
       object. This object may be of <code>Institute.Speciality</code> or of
      any of its subclasses. Cast to <code>Institute.Speciality</code> is
      guaranteed to pass without <code>ClassCastException</code> exception.

       @param db <code>Database</code> object used for connection with
       Cache database.

       @param id ID as specified in Cache represented as
      <code>Id</code>.

       @throws com.intersys.objects.CacheException in case of error.
      @see java.lang.ClassCastException
           @see #_open(com.intersys.objects.Database, com.intersys.objects.Oid)
           @see #open(com.intersys.objects.Database, com.intersys.objects.Oid)
           @see #Speciality
     */
    public static com.intersys.classes.RegisteredObject _open(com.intersys.objects.Database db, com.intersys.objects.Id id) throws com.intersys.objects.CacheException {
        return open(db, id);
    }

    /**
       Runs method <code> %OpenId </code> in Cache to open an object
       from Cache database and creates corresponding object of class
       <code>Institute.Speciality</code>.

       @return <code> RegisteredObject </code>, corresponding to opened
       object. This object may be of <code>Institute.Speciality</code> or of
      any of its subclasses. Cast to <code>Institute.Speciality</code> is
      guaranteed to pass without <code>ClassCastException</code> exception.

       @param db <code>Database</code> object used for connection with
       Cache database.

       @param id ID as specified in Cache represented as
      <code>Id</code>.

       @throws com.intersys.objects.CacheException in case of error.
      @see java.lang.ClassCastException
           @see #_open(com.intersys.objects.Database, com.intersys.objects.Oid)
           @see #open(com.intersys.objects.Database, com.intersys.objects.Oid)
           @see #Speciality
     */
    public static com.intersys.classes.RegisteredObject open(com.intersys.objects.Database db, com.intersys.objects.Id id) throws com.intersys.objects.CacheException {
        com.intersys.cache.CacheObject cobj = (((com.intersys.cache.SysDatabase) db).openCacheObject(CACHE_CLASS_NAME, id.toString()));
        return (com.intersys.classes.RegisteredObject) (cobj.newJavaInstance());
    }

    /**
       Runs method <code> %OpenId </code> in Cache to open an object
       from Cache database and creates corresponding object of class
       <code>Institute.Speciality</code>.

       @return <code> RegisteredObject </code>, corresponding to opened
       object. This object may be of <code>Institute.Speciality</code> or of
      any of its subclasses. Cast to <code>Institute.Speciality</code> is
      guaranteed to pass without <code>ClassCastException</code> exception.

       @param db <code>Database</code> object used for connection with
       Cache database.

       @param id ID as specified in Cache represented as
      <code>Id</code>.
      @param concurrency Concurrency level.  represented as
      <code>Concurrency</code>.

      Here are concurrency values, see Object Concurrency Options in your on-line Cache' documentation for more information.
      @see <a href = "http://cache-server:57772/csp/documatic/DocBook.UI.Page.cls?KEY=GOBJ_concurrency"> Object Concurrency Options.</A>

      <TABLE border="1"
      summary="Object Concurrency Options.">
      <CAPTION><EM>Object Concurrency Options</EM></CAPTION>
      <TR><TD>-1 </TD><TD>Default concurrency</TD></TR>
      <TR><TD>0 </TD><TD>No locking, no locks are used</TD></TR>
      <TR><TD>1 </TD><TD>Atomic</TD></TR>
      <TR><TD>2 </TD><TD>Shared</TD></TR>
      <TR><TD>3 </TD><TD>Shared/Retained</TD></TR>
      <TR><TD>4 </TD><TD>Exclusive</TD></TR>
      </TABLE>

       @throws com.intersys.objects.CacheException in case of error.
      @see java.lang.ClassCastException
           @see #_open(com.intersys.objects.Database, com.intersys.objects.Oid)
           @see #open(com.intersys.objects.Database, com.intersys.objects.Oid)
           @see #Speciality
     */
    public static com.intersys.classes.RegisteredObject _open(com.intersys.objects.Database db, com.intersys.objects.Id id, int concurrency) throws com.intersys.objects.CacheException {
        return open(db, id, concurrency);
    }

    /**
       Runs method <code> %OpenId </code> in Cache to open an object
       from Cache database and creates corresponding object of class
       <code>Institute.Speciality</code>.

       @return <code> RegisteredObject </code>, corresponding to opened
       object. This object may be of <code>Institute.Speciality</code> or of
      any of its subclasses. Cast to <code>Institute.Speciality</code> is
      guaranteed to pass without <code>ClassCastException</code> exception.

       @param db <code>Database</code> object used for connection with
       Cache database.

       @param id ID as specified in Cache represented as
      <code>Id</code>.
      @param concurrency Concurrency level.  represented as
      <code>Concurrency</code>.

      Here are concurrency values, see Object Concurrency Options in your on-line Cache' documentation for more information.
      @see <a href = "http://cache-server:57772/csp/documatic/DocBook.UI.Page.cls?KEY=GOBJ_concurrency"> Object Concurrency Options.</A>

      <TABLE border="1"
      summary="Object Concurrency Options.">
      <CAPTION><EM>Object Concurrency Options</EM></CAPTION>
      <TR><TD>-1 </TD><TD>Default concurrency</TD></TR>
      <TR><TD>0 </TD><TD>No locking, no locks are used</TD></TR>
      <TR><TD>1 </TD><TD>Atomic</TD></TR>
      <TR><TD>2 </TD><TD>Shared</TD></TR>
      <TR><TD>3 </TD><TD>Shared/Retained</TD></TR>
      <TR><TD>4 </TD><TD>Exclusive</TD></TR>
      </TABLE>

       @throws com.intersys.objects.CacheException in case of error.
      @see java.lang.ClassCastException
           @see #_open(com.intersys.objects.Database, com.intersys.objects.Oid)
           @see #open(com.intersys.objects.Database, com.intersys.objects.Oid)
           @see #Speciality
     */
    public static com.intersys.classes.RegisteredObject open(com.intersys.objects.Database db, com.intersys.objects.Id id, int concurrency) throws com.intersys.objects.CacheException {
        com.intersys.cache.CacheObject cobj = (((com.intersys.cache.SysDatabase) db).openCacheObject(CACHE_CLASS_NAME, id.toString(), concurrency));
        return (com.intersys.classes.RegisteredObject) (cobj.newJavaInstance());
    }

    /**
       Runs method <code> %Open </code> in Cache to open an object
       from Cache database and creates corresponding object of class
       <code>Institute.Speciality</code>.

       @return <code> RegisteredObject </code>, corresponding to opened
       object. This object may be of <code>Institute.Speciality</code> or of
      any of its subclasses. Cast to <code>Institute.Speciality</code> is
      guaranteed to pass without <code>ClassCastException</code> exception.

       @param db <code>Database</code> object used for connection with
       Cache database.
       @param oid Object ID as specified in Cache. represented as
      <code>Oid</code>.


       @throws com.intersys.objects.CacheException in case of error.
      @see java.lang.ClassCastException
           @see #_open(com.intersys.objects.Database, com.intersys.objects.Oid)
           @see #open(com.intersys.objects.Database, com.intersys.objects.Oid)
           @see #Speciality(com.intersys.objects.Database)
     */
    public static com.intersys.classes.RegisteredObject _open(com.intersys.objects.Database db, com.intersys.objects.Oid oid) throws com.intersys.objects.CacheException {
        return open(db, oid);
    }

    /**
       Runs method <code> %Open </code> in Cache to open an object
       from Cache database and creates corresponding object of class
       <code>Institute.Speciality</code>.

       @return <code> RegisteredObject </code>, corresponding to opened
       object. This object may be of <code>Institute.Speciality</code> or of
      any of its subclasses. Cast to <code>Institute.Speciality</code> is
      guaranteed to pass without <code>ClassCastException</code> exception.

       @param db <code>Database</code> object used for connection with
       Cache database.
       @param oid Object ID as specified in Cache. represented as
      <code>Oid</code>.


       @throws com.intersys.objects.CacheException in case of error.
      @see java.lang.ClassCastException
           @see #_open(com.intersys.objects.Database, com.intersys.objects.Oid)
           @see #open(com.intersys.objects.Database, com.intersys.objects.Oid)
           @see #Speciality(com.intersys.objects.Database)
     */
    public static com.intersys.classes.RegisteredObject open(com.intersys.objects.Database db, com.intersys.objects.Oid oid) throws com.intersys.objects.CacheException {
        com.intersys.cache.CacheObject cobj = (((com.intersys.cache.SysDatabase) db).openCacheObject(CACHE_CLASS_NAME, oid.getData()));
        return (com.intersys.classes.RegisteredObject) (cobj.newJavaInstance());
    }

    /**
       Runs method <code> %Open </code> in Cache to open an object
       from Cache database and creates corresponding object of class
       <code>Institute.Speciality</code>.

       @return <code> RegisteredObject </code>, corresponding to opened
       object. This object may be of <code>Institute.Speciality</code> or of
      any of its subclasses. Cast to <code>Institute.Speciality</code> is
      guaranteed to pass without <code>ClassCastException</code> exception.

       @param db <code>Database</code> object used for connection with
       Cache database.
       @param oid Object ID as specified in Cache. represented as
      <code>Oid</code>.
      @param concurrency Concurrency level.  represented as
      <code>Concurrency</code>.

      Here are concurrency values, see Object Concurrency Options in your on-line Cache' documentation for more information.
      @see <a href = "http://cache-server:57772/csp/documatic/DocBook.UI.Page.cls?KEY=GOBJ_concurrency"> Object Concurrency Options.</A>

      <TABLE border="1"
      summary="Object Concurrency Options.">
      <CAPTION><EM>Object Concurrency Options</EM></CAPTION>
      <TR><TD>-1 </TD><TD>Default concurrency</TD></TR>
      <TR><TD>0 </TD><TD>No locking, no locks are used</TD></TR>
      <TR><TD>1 </TD><TD>Atomic</TD></TR>
      <TR><TD>2 </TD><TD>Shared</TD></TR>
      <TR><TD>3 </TD><TD>Shared/Retained</TD></TR>
      <TR><TD>4 </TD><TD>Exclusive</TD></TR>
      </TABLE>

      @throws com.intersys.objects.CacheException in case of error.
      @see java.lang.ClassCastException
           @see #_open(com.intersys.objects.Database, com.intersys.objects.Oid)
           @see #open(com.intersys.objects.Database, com.intersys.objects.Oid)
           @see #Speciality(com.intersys.objects.Database)
     */
    public static com.intersys.classes.RegisteredObject _open(com.intersys.objects.Database db, com.intersys.objects.Oid oid, int concurrency) throws com.intersys.objects.CacheException {
        return open(db, oid, concurrency);
    }

    /**
       Runs method <code> %Open </code> in Cache to open an object
       from Cache database and creates corresponding object of class
       <code>Institute.Speciality</code>.

       @return <code> RegisteredObject </code>, corresponding to opened
       object. This object may be of <code>Institute.Speciality</code> or of
      any of its subclasses. Cast to <code>Institute.Speciality</code> is
      guaranteed to pass without <code>ClassCastException</code> exception.

       @param db <code>Database</code> object used for connection with
       Cache database.
       @param oid Object ID as specified in Cache. represented as
      <code>Oid</code>.
      @param concurrency Concurrency level.  represented as
      <code>Concurrency</code>.

      Here are concurrency values, see Object Concurrency Options in your on-line Cache' documentation for more information.
      @see <a href = "http://cache-server:57772/csp/documatic/DocBook.UI.Page.cls?KEY=GOBJ_concurrency"> Object Concurrency Options.</A>

      <TABLE border="1"
      summary="Object Concurrency Options.">
      <CAPTION><EM>Object Concurrency Options</EM></CAPTION>
      <TR><TD>-1 </TD><TD>Default concurrency</TD></TR>
      <TR><TD>0 </TD><TD>No locking, no locks are used</TD></TR>
      <TR><TD>1 </TD><TD>Atomic</TD></TR>
      <TR><TD>2 </TD><TD>Shared</TD></TR>
      <TR><TD>3 </TD><TD>Shared/Retained</TD></TR>
      <TR><TD>4 </TD><TD>Exclusive</TD></TR>
      </TABLE>

      @throws com.intersys.objects.CacheException in case of error.
      @see java.lang.ClassCastException
           @see #_open(com.intersys.objects.Database, com.intersys.objects.Oid)
           @see #open(com.intersys.objects.Database, com.intersys.objects.Oid)
           @see #Speciality(com.intersys.objects.Database)
     */
    public static com.intersys.classes.RegisteredObject open(com.intersys.objects.Database db, com.intersys.objects.Oid oid, int concurrency) throws com.intersys.objects.CacheException {
        com.intersys.cache.CacheObject cobj = (((com.intersys.cache.SysDatabase) db).openCacheObject(CACHE_CLASS_NAME, oid.getData(), concurrency));
        return (com.intersys.classes.RegisteredObject) (cobj.newJavaInstance());
    }

    /**
    * Calls method
    * {@link com.intersys.objects.Database#openByQuery(String, String, Object[])}
    * to open all instances of class Institute.Speciality
    * that satisfy given simple
    * condition, specified in SQL syntax.
    *
    * @param db <code>Database</code> object used for connection with
    * Cache database.
    *
    * @param condition Simple condition on objects, in SQL syntax. It
    * can refer only to properties of a single object.
    *
    * @param args Parameters to replace "?" in
    * <code>condition</code>. If there are no parameters in condition
    * may be null or empty array.
    *
    * @return Iterator in the form of
    * <code>java.util.Iterator</code>, that iterates over all opened
    * objects in specified order.
    *
    * @see #openByQuery(com.intersys.objects.Database, String)
    * @see com.intersys.objects.Database#openByQuery(String, String, Object[])
    * @see com.intersys.objects.Database#openByQuery(String, String)
    * @see com.intersys.objects.Database#openByQuery(String, Object[])
    * @see com.intersys.objects.Database#openByQuery(String)
    *
    */
    public static java.util.Iterator openByQuery(com.intersys.objects.Database db, String condition, Object[] args) throws com.intersys.objects.CacheException {
        return db.openByQuery(CACHE_CLASS_NAME, condition, args);
    }

    /**
    * Calls method
    * {@link com.intersys.objects.Database#openByQuery(String, String)}
    * to open all instances of class Sample.Person
    * that satisfy given simple
    * condition, specified in SQL syntax.
    *
    * @param db <code>Database</code> object used for connection with
    * Cache database.
    *
    * @param condition Simple condition on objects, in SQL syntax. It
    * can refer only to properties of a single object.
    *
    * @return Iterator in the form of
    * <code>java.util.Iterator</code>, that iterates over all opened
    * objects in specified order.
    *
    * @see #openByQuery(com.intersys.objects.Database, String, Object[])
    * @see com.intersys.objects.Database#openByQuery(String, String)
    * @see com.intersys.objects.Database#openByQuery(String, String, Object[])
    * @see com.intersys.objects.Database#openByQuery(String, Object[])
    * @see com.intersys.objects.Database#openByQuery(String)
    *
    */
    public static java.util.Iterator openByQuery(com.intersys.objects.Database db, String condition) throws com.intersys.objects.CacheException {
        return db.openByQuery(CACHE_CLASS_NAME, condition);
    }

    /**
    * Calls method
    * {@link com.intersys.objects.Database#saveObjects(String,
    java.util.Collection)}
    * to save a collection of previously created new objects in Cache
    * Database. All object in
    * collection must already exist in database. All
    * objects must be instances of <code>Institute.Speciality</code> and not of its
    * subclasses. To save
    * changes in newly created objects use <code>createObjects</code>
    * method or instance method <code>save</code> on the object
    * itself.
    *
    * @param db <code>Database</code> object used for connection with
    * Cache database.
    *
    * @param objects A collection of newly created objects. None of
    * the objects in the collection may already exist in database.
    *
    * @see #saveObjects(com.intersys.objects.Database, java.util.Collection)
    * @see com.intersys.objects.Database#createObjects(String, java.util.Collection)
    *
    */
    public static void saveObjects(com.intersys.objects.Database db, java.util.Collection objects) throws com.intersys.objects.CacheException {
        db.saveObjects(CACHE_CLASS_NAME, objects);
    }

    /**
       Runs method <code> %Delete </code> in Cache to delete an object
       from Cache database.

       Deletes the stored version of the object with OID <var>oid</var> from the database.
       It does not remove any in-memory versions of the object that may be present.

       @param db <code>Database</code> object used for connection with
       Cache database.

       @param id ID as specified in Cache represented as
       <code>Id</code>.

       @throws com.intersys.objects.CacheException in case of error.
      @see java.lang.ClassCastException
           @see #_deleteId(com.intersys.objects.Database, com.intersys.objects.Id)
           @see #Speciality
     */
    public static void delete(com.intersys.objects.Database db, com.intersys.objects.Id id) throws com.intersys.objects.CacheException {
        ((com.intersys.cache.SysDatabase) db).deleteObject(CACHE_CLASS_NAME, id);
    }

    /**
       Runs method <code> %Delete </code> in Cache to delete an object
       from Cache database.

       Deletes the stored version of the object with OID <var>oid</var> from the database.
       It does not remove any in-memory versions of the object that may be present.

       @param db <code>Database</code> object used for connection with
       Cache database.

       @param id ID as specified in Cache represented as
       <code>Id</code>.
       @param concurrency Concurrency level.  represented as
       <code>Concurrency</code>.

      Here are concurrency values, see Object Concurrency Options in your on-line Cache' documentation for more information.
      @see <a href = "http://cache-server:57772/csp/documatic/DocBook.UI.Page.cls?KEY=GOBJ_concurrency"> Object Concurrency Options.</A>

      <TABLE border="1"
      summary="Object Concurrency Options.">
      <CAPTION><EM>Object Concurrency Options</EM></CAPTION>
      <TR><TD>-1 </TD><TD>Default concurrency</TD></TR>
      <TR><TD>0 </TD><TD>No locking, no locks are used</TD></TR>
      <TR><TD>1 </TD><TD>Atomic</TD></TR>
      <TR><TD>2 </TD><TD>Shared</TD></TR>
      <TR><TD>3 </TD><TD>Shared/Retained</TD></TR>
      <TR><TD>4 </TD><TD>Exclusive</TD></TR>
      </TABLE>


       @throws com.intersys.objects.CacheException in case of error.
      @see java.lang.ClassCastException
           @see #_deleteId(com.intersys.objects.Database, com.intersys.objects.Id)
           @see #Speciality
     */
    public static void delete(com.intersys.objects.Database db, com.intersys.objects.Id id, int concurrency) throws com.intersys.objects.CacheException {
        ((com.intersys.cache.SysDatabase) db).deleteObject(CACHE_CLASS_NAME, id, concurrency);
    }

    /**
       Runs method <code> %Delete </code> in Cache to delete an object
       from Cache database.

       Deletes the stored version of the object with OID <var>oid</var> from the database.
       It does not remove any in-memory versions of the object that may be present.

       @param db <code>Database</code> object used for connection with
       Cache database.

       @param id ID as specified in Cache represented as
       <code>Id</code>.

       @throws com.intersys.objects.CacheException in case of error.
      @see java.lang.ClassCastException
           @see #_deleteId(com.intersys.objects.Database, com.intersys.objects.Id)
           @see #Speciality
     */
    public static void _deleteId(com.intersys.objects.Database db, com.intersys.objects.Id id) throws com.intersys.objects.CacheException {
        delete(db, id);
    }

    /**
       Runs method <code> %Delete </code> in Cache to delete an object
       from Cache database.

       Deletes the stored version of the object with OID <var>oid</var> from the database.
       It does not remove any in-memory versions of the object that may be present.

       @param db <code>Database</code> object used for connection with
       Cache database.

       @param id ID as specified in Cache represented as
       <code>Id</code>.
       @param concurrency Concurrency level.  represented as
       <code>Concurrency</code>.

      Here are concurrency values, see Object Concurrency Options in your on-line Cache' documentation for more information.
      @see <a href = "http://cache-server:57772/csp/documatic/DocBook.UI.Page.cls?KEY=GOBJ_concurrency"> Object Concurrency Options.</A>

      <TABLE border="1"
      summary="Object Concurrency Options.">
      <CAPTION><EM>Object Concurrency Options</EM></CAPTION>
      <TR><TD>-1 </TD><TD>Default concurrency</TD></TR>
      <TR><TD>0 </TD><TD>No locking, no locks are used</TD></TR>
      <TR><TD>1 </TD><TD>Atomic</TD></TR>
      <TR><TD>2 </TD><TD>Shared</TD></TR>
      <TR><TD>3 </TD><TD>Shared/Retained</TD></TR>
      <TR><TD>4 </TD><TD>Exclusive</TD></TR>
      </TABLE>


       @throws com.intersys.objects.CacheException in case of error.
      @see java.lang.ClassCastException
           @see #_deleteId(com.intersys.objects.Database, com.intersys.objects.Id)
           @see #Speciality
     */
    public static void _deleteId(com.intersys.objects.Database db, com.intersys.objects.Id id, int concurrency) throws com.intersys.objects.CacheException {
        delete(db, id, concurrency);
    }

    /**
       Runs method <code> %Exists </code> in Cache to see if an object exists.

       @param db <code>Database</code> object used for connection with
       Cache database.

       @param id ID as specified in Cache represented as
      <code>Id</code>.

       @throws com.intersys.objects.CacheException in case of error.
      @see java.lang.ClassCastException
           @see #_existsId(com.intersys.objects.Database, com.intersys.objects.Id)
           @see #Speciality
     */
    public static boolean exists(com.intersys.objects.Database db, com.intersys.objects.Id id) throws com.intersys.objects.CacheException {
        return ((com.intersys.cache.SysDatabase) db).existsObject(CACHE_CLASS_NAME, id);
    }

    /**
       Runs method <code> %Exists </code> in Cache to see if an object exists.

       @param db <code>Database</code> object used for connection with
       Cache database.

       @param id ID as specified in Cache represented as
      <code>Id</code>.

       @throws com.intersys.objects.CacheException in case of error.
      @see java.lang.ClassCastException
           @see #_existsId(com.intersys.objects.Database, com.intersys.objects.Id)
           @see #Speciality
     */
    public static Boolean _existsId(com.intersys.objects.Database db, com.intersys.objects.Id id) throws com.intersys.objects.CacheException {
        return new Boolean(exists(db, id));
    }

    /**
       Returns class name of the class Institute.Speciality as it is in
      Cache Database. Note, that this is a static method, so no
      object specific information can be returned. Use
      <code>getCacheClass().getName()</code> to get the class name
      for specific object.
       @return Cache class name as a <code>String</code>
      @see #getCacheClass()
      @see com.intersys.objects.reflect.CacheClass#getName()
     */
    public static String getCacheClassName() {
        return CACHE_CLASS_NAME;
    }

    /**
           Allows access metadata information about type of this object
           in Cache database. Also can be used for dynamic binding (accessing
           properties and calling methods without particular class known).

           @return <code>CacheClass</code> object for this object type.
   */
    public com.intersys.objects.reflect.CacheClass getCacheClass() throws com.intersys.objects.CacheException {
        return mInternal.getCacheClass();
    }

    /**
       Verifies that all fields from Cache class are exposed with
       accessor methods in Java class and that values for indexes in
       zObjVal are the same as in Cache. It does not return anything
       but it throws an exception in case of inconsistency.

       <p>But if there is any inconsistency in zObjVal indexes this is fatal and class can not work correctly and must be regenerated

       @param db Database used for connection. Note that if you are
       using multiple databases the class can be consistent with one
       and inconsistent with another.
       @throws com.intersys.objects.InvalidClassException if any inconsistency is found.
       @throws com.intersys.objects.CacheException if any error occurred during
       verification, e.g. communication error with Database.
       @see com.intersys.objects.InvalidPropertyException

     */
    public static void checkAllFieldsValid(com.intersys.objects.Database db) throws com.intersys.objects.CacheException {
        checkAllFieldsValid(db, CACHE_CLASS_NAME, Speciality.class);
    }

    /**
       Runs method <code> %Exists </code> in Cache to see if an object exists.

       @return <code> RegisteredObject </code>, corresponding to opened
       object. This object may be of <code>Institute.Speciality</code> or of
      any of its subclasses. Cast to <code>Institute.Speciality</code> is
      guaranteed to pass without <code>ClassCastException</code> exception.

       @param db <code>Database</code> object used for connection with
       Cache database.

       @param oid Object ID as specified in Cache. represented as
      <code>Oid</code>.

       @throws com.intersys.objects.CacheException in case of error.
      @see java.lang.ClassCastException
           @see #_open(com.intersys.objects.Database, com.intersys.objects.Oid)
           @see #open(com.intersys.objects.Database, com.intersys.objects.Oid)
           @see #Speciality
     */
    public static boolean exists(com.intersys.objects.Database db, com.intersys.objects.Oid oid) throws com.intersys.objects.CacheException {
        return exists(db, oid, CACHE_CLASS_NAME);
    }

    /**
       Verifies that all fields from Cache class are exposed with
       accessor methods in Java class and that values for indexes in
       zObjVal are the same as in Cache. It does not return anything
       but it throws an exception in case of inconsistency.

       <p>But if there is any inconsistency in zObjVal indexes this is fatal and class can not work correctly and must be regenerated

       @param db Database used for connection. Note that if you are
       using multiple databases the class can be consistent with one
       and inconsistent with another.
       @throws com.intersys.objects.InvalidClassException if any inconsistency is found.
       @throws com.intersys.objects.CacheException if any error occurred during
       verification, e.g. communication error with Database.
       @see com.intersys.objects.InvalidPropertyException

     */
    public static void checkAllMethods(com.intersys.objects.Database db) throws com.intersys.objects.CacheException {
        checkAllMethods(db, CACHE_CLASS_NAME, Speciality.class);
    }

    private static int ii_Groups = 7;

    private static int jj_Groups = 0;

    private static int kk_Groups = 7;

    /**
       Verifies that indexes for property <code>Groups</code> in
       zObjVal are the same as in Cache. It does not return anything
       but it throws an exception in case of inconsistency.

       <p> Please note, that if there is any inconsistency in zObjVal
       indexes this is fatal and class can not work correctly and must
       be regenerated.

       @param db Database used for connection. Note that if you are
       using multiple databases the class can be consistent with one
       and inconsistent with another.
       @throws com.intersys.objects.InvalidClassException if any inconsistency is found.
       @throws com.intersys.objects.CacheException if any error occurred during
       verification, e.g. communication error with Database.
       @see #checkAllFieldsValid

     */
    public static void checkGroupsValid(com.intersys.objects.Database db) throws com.intersys.objects.CacheException {
        checkZobjValid(db, CACHE_CLASS_NAME, "Groups", ii_Groups, jj_Groups, kk_Groups);
    }

    /**
       Returns value of property <code>Groups</code>.
       <Description>
       @return current value of <code>Groups</code> represented as
       <code>com.intersys.classes.RelationshipObject</code>

       @throws com.intersys.objects.CacheException if any error occurred during value retrieval.
       @see <a href = "http://cache-server:57772/csp/documatic/%25CSP.Documatic.cls?APP=1&PAGE=CLASS&LIBRARY=TPCS&CLASSNAME=Institute.Speciality#Groups"> Groups</A>
    */
    public com.intersys.classes.RelationshipObject getGroups() throws com.intersys.objects.CacheException {
        com.intersys.cache.Dataholder dh = mInternal.getProperty(ii_Groups, jj_Groups, com.intersys.objects.Database.RET_OBJECT, "Groups");
        com.intersys.cache.CacheObject cobj = dh.getCacheObject();
        if (cobj == null) return null;
        return (com.intersys.classes.RelationshipObject) (cobj.newJavaInstance());
    }

    private static int ii_Name = 2;

    private static int jj_Name = 0;

    private static int kk_Name = 2;

    /**
       Verifies that indexes for property <code>Name</code> in
       zObjVal are the same as in Cache. It does not return anything
       but it throws an exception in case of inconsistency.

       <p> Please note, that if there is any inconsistency in zObjVal
       indexes this is fatal and class can not work correctly and must
       be regenerated.

       @param db Database used for connection. Note that if you are
       using multiple databases the class can be consistent with one
       and inconsistent with another.
       @throws com.intersys.objects.InvalidClassException if any inconsistency is found.
       @throws com.intersys.objects.CacheException if any error occurred during
       verification, e.g. communication error with Database.
       @see #checkAllFieldsValid

     */
    public static void checkNameValid(com.intersys.objects.Database db) throws com.intersys.objects.CacheException {
        checkZobjValid(db, CACHE_CLASS_NAME, "Name", ii_Name, jj_Name, kk_Name);
    }

    /**
       Returns value of property <code>Name</code>.
       <Description>
       @return current value of <code>Name</code> represented as
       <code>java.lang.String</code>

       @throws com.intersys.objects.CacheException if any error occurred during value retrieval.
       @see <a href = "http://cache-server:57772/csp/documatic/%25CSP.Documatic.cls?APP=1&PAGE=CLASS&LIBRARY=TPCS&CLASSNAME=Institute.Speciality#Name"> Name</A>
    */
    public java.lang.String getName() throws com.intersys.objects.CacheException {
        com.intersys.cache.Dataholder dh = mInternal.getProperty(ii_Name, jj_Name, com.intersys.objects.Database.RET_PRIM, "Name");
        return dh.getString();
    }

    /**
       Sets new value for <code>Name</code>.
       <Description>
       @param value new value to be set represented as
       <code>java.lang.String</code>.
       @throws com.intersys.objects.CacheException if any error occurred during value setting.
       @see <a href = "http://cache-server:57772/csp/documatic/%25CSP.Documatic.cls?APP=1&PAGE=CLASS&LIBRARY=TPCS&CLASSNAME=Institute.Speciality#Name"> Name</A>
    */
    public void setName(java.lang.String value) throws com.intersys.objects.CacheException {
        com.intersys.cache.Dataholder dh = new com.intersys.cache.Dataholder(value);
        mInternal.setProperty(ii_Name, jj_Name, kk_Name, com.intersys.objects.Database.RET_PRIM, "Name", dh);
        return;
    }

    private static int ii_Number = 9;

    private static int jj_Number = 0;

    private static int kk_Number = 9;

    /**
       Verifies that indexes for property <code>Number</code> in
       zObjVal are the same as in Cache. It does not return anything
       but it throws an exception in case of inconsistency.

       <p> Please note, that if there is any inconsistency in zObjVal
       indexes this is fatal and class can not work correctly and must
       be regenerated.

       @param db Database used for connection. Note that if you are
       using multiple databases the class can be consistent with one
       and inconsistent with another.
       @throws com.intersys.objects.InvalidClassException if any inconsistency is found.
       @throws com.intersys.objects.CacheException if any error occurred during
       verification, e.g. communication error with Database.
       @see #checkAllFieldsValid

     */
    public static void checkNumberValid(com.intersys.objects.Database db) throws com.intersys.objects.CacheException {
        checkZobjValid(db, CACHE_CLASS_NAME, "Number", ii_Number, jj_Number, kk_Number);
    }

    /**
       Returns value of property <code>Number</code>.
       <Description>
       @return current value of <code>Number</code> represented as
       <code>java.lang.String</code>

       @throws com.intersys.objects.CacheException if any error occurred during value retrieval.
       @see <a href = "http://cache-server:57772/csp/documatic/%25CSP.Documatic.cls?APP=1&PAGE=CLASS&LIBRARY=TPCS&CLASSNAME=Institute.Speciality#Number"> Number</A>
    */
    public java.lang.String getNumber() throws com.intersys.objects.CacheException {
        com.intersys.cache.Dataholder dh = mInternal.getProperty(ii_Number, jj_Number, com.intersys.objects.Database.RET_PRIM, "Number");
        return dh.getString();
    }

    /**
       Sets new value for <code>Number</code>.
       <Description>
       @param value new value to be set represented as
       <code>java.lang.String</code>.
       @throws com.intersys.objects.CacheException if any error occurred during value setting.
       @see <a href = "http://cache-server:57772/csp/documatic/%25CSP.Documatic.cls?APP=1&PAGE=CLASS&LIBRARY=TPCS&CLASSNAME=Institute.Speciality#Number"> Number</A>
    */
    public void setNumber(java.lang.String value) throws com.intersys.objects.CacheException {
        com.intersys.cache.Dataholder dh = new com.intersys.cache.Dataholder(value);
        mInternal.setProperty(ii_Number, jj_Number, kk_Number, com.intersys.objects.Database.RET_PRIM, "Number", dh);
        return;
    }

    private static int ii_Semester = 1;

    private static int jj_Semester = 0;

    private static int kk_Semester = 1;

    /**
       Verifies that indexes for property <code>Semester</code> in
       zObjVal are the same as in Cache. It does not return anything
       but it throws an exception in case of inconsistency.

       <p> Please note, that if there is any inconsistency in zObjVal
       indexes this is fatal and class can not work correctly and must
       be regenerated.

       @param db Database used for connection. Note that if you are
       using multiple databases the class can be consistent with one
       and inconsistent with another.
       @throws com.intersys.objects.InvalidClassException if any inconsistency is found.
       @throws com.intersys.objects.CacheException if any error occurred during
       verification, e.g. communication error with Database.
       @see #checkAllFieldsValid

     */
    public static void checkSemesterValid(com.intersys.objects.Database db) throws com.intersys.objects.CacheException {
        checkZobjValid(db, CACHE_CLASS_NAME, "Semester", ii_Semester, jj_Semester, kk_Semester);
    }

    /**
       Returns value of property <code>Semester</code>.
       <Description>
       @return current value of <code>Semester</code> represented as
       <code>java.lang.Integer</code>

       @throws com.intersys.objects.CacheException if any error occurred during value retrieval.
       @see <a href = "http://cache-server:57772/csp/documatic/%25CSP.Documatic.cls?APP=1&PAGE=CLASS&LIBRARY=TPCS&CLASSNAME=Institute.Speciality#Semester"> Semester</A>
    */
    public java.lang.Integer getSemester() throws com.intersys.objects.CacheException {
        com.intersys.cache.Dataholder dh = mInternal.getProperty(ii_Semester, jj_Semester, com.intersys.objects.Database.RET_PRIM, "Semester");
        return dh.getInteger();
    }

    /**
       Sets new value for <code>Semester</code>.
       <Description>
       @param value new value to be set represented as
       <code>java.lang.Integer</code>.
       @throws com.intersys.objects.CacheException if any error occurred during value setting.
       @see <a href = "http://cache-server:57772/csp/documatic/%25CSP.Documatic.cls?APP=1&PAGE=CLASS&LIBRARY=TPCS&CLASSNAME=Institute.Speciality#Semester"> Semester</A>
    */
    public void setSemester(java.lang.Integer value) throws com.intersys.objects.CacheException {
        com.intersys.cache.Dataholder dh = new com.intersys.cache.Dataholder(value);
        mInternal.setProperty(ii_Semester, jj_Semester, kk_Semester, com.intersys.objects.Database.RET_PRIM, "Semester", dh);
        return;
    }

    private static int ii_Subjects = 3;

    private static int jj_Subjects = 0;

    private static int kk_Subjects = 3;

    /**
       Verifies that indexes for property <code>Subjects</code> in
       zObjVal are the same as in Cache. It does not return anything
       but it throws an exception in case of inconsistency.

       <p> Please note, that if there is any inconsistency in zObjVal
       indexes this is fatal and class can not work correctly and must
       be regenerated.

       @param db Database used for connection. Note that if you are
       using multiple databases the class can be consistent with one
       and inconsistent with another.
       @throws com.intersys.objects.InvalidClassException if any inconsistency is found.
       @throws com.intersys.objects.CacheException if any error occurred during
       verification, e.g. communication error with Database.
       @see #checkAllFieldsValid

     */
    public static void checkSubjectsValid(com.intersys.objects.Database db) throws com.intersys.objects.CacheException {
        checkZobjValid(db, CACHE_CLASS_NAME, "Subjects", ii_Subjects, jj_Subjects, kk_Subjects);
    }

    /**
       Returns value of property <code>Subjects</code>.
       <Description>
       @return current value of <code>Subjects</code> represented as
       <code>com.intersys.classes.RelationshipObject</code>

       @throws com.intersys.objects.CacheException if any error occurred during value retrieval.
       @see <a href = "http://cache-server:57772/csp/documatic/%25CSP.Documatic.cls?APP=1&PAGE=CLASS&LIBRARY=TPCS&CLASSNAME=Institute.Speciality#Subjects"> Subjects</A>
    */
    public com.intersys.classes.RelationshipObject getSubjects() throws com.intersys.objects.CacheException {
        com.intersys.cache.Dataholder dh = mInternal.getProperty(ii_Subjects, jj_Subjects, com.intersys.objects.Database.RET_OBJECT, "Subjects");
        com.intersys.cache.CacheObject cobj = dh.getCacheObject();
        if (cobj == null) return null;
        return (com.intersys.classes.RelationshipObject) (cobj.newJavaInstance());
    }

    /**
     <p>Runs method %ClassName in Cache.</p>
     <p>Description: Returns the object's class name. The <var>fullname</var> determines how the
class name is represented. If it is 1 then it returns the full class name
including any package qualifier. If it is 0 (the default) then it returns the
name of the class without the package, this is mainly for backward compatibility
with the pre-package behaviour of %ClassName.</p>
     @param db represented as com.intersys.objects.Database
     @param fullname represented as java.lang.Boolean
     @throws com.intersys.objects.CacheException if any error occured while running the method.
     @see <a href = "http://cache-server:57772/csp/documatic/%25CSP.Documatic.cls?APP=1&PAGE=CLASS&LIBRARY=TPCS&CLASSNAME=Institute.Speciality#%ClassName"> Method %ClassName</A>
    */
    public static java.lang.String sys_ClassName(com.intersys.objects.Database db, java.lang.Boolean fullname) throws com.intersys.objects.CacheException {
        com.intersys.cache.Dataholder[] args = new com.intersys.cache.Dataholder[1];
        args[0] = new com.intersys.cache.Dataholder(fullname);
        com.intersys.cache.Dataholder res = db.runClassMethod(CACHE_CLASS_NAME, "%ClassName", args, com.intersys.objects.Database.RET_PRIM);
        return res.getString();
    }

    /**
     <p>Runs method %ComposeOid in Cache.</p>
     <p>Description: %ComposeOid()

		This class method accepts an ID and returns a fully qualified OID, containing the most specific type class of the object identified by the
		<var>id</var> argument. If default storage is used then the %%CLASSNAME value is used to determine the most specific type class, otherwise if
		%OnDetermineClass is runnable then it is called to determine the most specific type class. If neither case is true then the current class is
		used. If there is a problem determining the most specific type class then null ("") is returned.</p>
     @param db represented as com.intersys.objects.Database
     @param id represented as java.lang.String
     @throws com.intersys.objects.CacheException if any error occured while running the method.
     @see <a href = "http://cache-server:57772/csp/documatic/%25CSP.Documatic.cls?APP=1&PAGE=CLASS&LIBRARY=TPCS&CLASSNAME=Institute.Speciality#%ComposeOid"> Method %ComposeOid</A>
    */
    public static void sys_ComposeOid(com.intersys.objects.Database db, java.lang.String id) throws com.intersys.objects.CacheException {
        com.intersys.cache.Dataholder[] args = new com.intersys.cache.Dataholder[1];
        args[0] = new com.intersys.cache.Dataholder(id);
        com.intersys.cache.Dataholder res = db.runClassMethod(CACHE_CLASS_NAME, "%ComposeOid", args, com.intersys.objects.Database.RET_NONE);
        return;
    }

    /**
     <p>Runs method %Delete in Cache.</p>
     <p>Description: Deletes the stored version of the object with OID <var>oid</var> from the database. 
It does not remove any in-memory versions of the object that may be present.

Refer to <LINK href=/AboutConcurrency.html>About Concurrency</LINK> for more details 
on the optional <var>concurrency</var> argument.

<p>Returns a <CLASS>%Status</CLASS> value indicating success or failure.

<p>Internally, <METHOD>%Delete</METHOD> initiates a transaction and then invokes the storage 
interface method <METHOD>%DeleteData</METHOD>. If <METHOD>%DeleteData</METHOD> succeeds, the 
transaction is committed, otherwise it is rolled back. </p>
     @param db represented as com.intersys.objects.Database
     default argument oid set to ""
     default argument concurrency set to -1
     @throws com.intersys.objects.CacheException if any error occured while running the method.
     @see #sys_Delete(com.intersys.objects.Database,com.intersys.objects.Oid,java.lang.Integer)
     @see <a href = "http://cache-server:57772/csp/documatic/%25CSP.Documatic.cls?APP=1&PAGE=CLASS&LIBRARY=TPCS&CLASSNAME=Institute.Speciality#%Delete"> Method %Delete</A>
    */
    public static void sys_Delete(com.intersys.objects.Database db) throws com.intersys.objects.CacheException {
        com.intersys.cache.Dataholder[] args = new com.intersys.cache.Dataholder[0];
        com.intersys.cache.Dataholder res = db.runClassMethod(CACHE_CLASS_NAME, "%Delete", args, com.intersys.objects.Database.RET_PRIM);
        db.parseStatus(res);
        return;
    }

    /**
     <p>Runs method %Delete in Cache.</p>
     <p>Description: Deletes the stored version of the object with OID <var>oid</var> from the database. 
It does not remove any in-memory versions of the object that may be present.

Refer to <LINK href=/AboutConcurrency.html>About Concurrency</LINK> for more details 
on the optional <var>concurrency</var> argument.

<p>Returns a <CLASS>%Status</CLASS> value indicating success or failure.

<p>Internally, <METHOD>%Delete</METHOD> initiates a transaction and then invokes the storage 
interface method <METHOD>%DeleteData</METHOD>. If <METHOD>%DeleteData</METHOD> succeeds, the 
transaction is committed, otherwise it is rolled back. </p>
     @param db represented as com.intersys.objects.Database
     @param oid represented as com.intersys.objects.Oid
     default argument concurrency set to -1
     @throws com.intersys.objects.CacheException if any error occured while running the method.
     @see #sys_Delete(com.intersys.objects.Database,com.intersys.objects.Oid,java.lang.Integer)
     @see <a href = "http://cache-server:57772/csp/documatic/%25CSP.Documatic.cls?APP=1&PAGE=CLASS&LIBRARY=TPCS&CLASSNAME=Institute.Speciality#%Delete"> Method %Delete</A>
    */
    public static void sys_Delete(com.intersys.objects.Database db, com.intersys.objects.Oid oid) throws com.intersys.objects.CacheException {
        com.intersys.cache.Dataholder[] args = new com.intersys.cache.Dataholder[1];
        args[0] = new com.intersys.cache.Dataholder(oid);
        com.intersys.cache.Dataholder res = db.runClassMethod(CACHE_CLASS_NAME, "%Delete", args, com.intersys.objects.Database.RET_PRIM);
        db.parseStatus(res);
        return;
    }

    /**
     <p>Runs method %Delete in Cache.</p>
     <p>Description: Deletes the stored version of the object with OID <var>oid</var> from the database. 
It does not remove any in-memory versions of the object that may be present.

Refer to <LINK href=/AboutConcurrency.html>About Concurrency</LINK> for more details 
on the optional <var>concurrency</var> argument.

<p>Returns a <CLASS>%Status</CLASS> value indicating success or failure.

<p>Internally, <METHOD>%Delete</METHOD> initiates a transaction and then invokes the storage 
interface method <METHOD>%DeleteData</METHOD>. If <METHOD>%DeleteData</METHOD> succeeds, the 
transaction is committed, otherwise it is rolled back. </p>
     @param db represented as com.intersys.objects.Database
     @param oid represented as com.intersys.objects.Oid
     @param concurrency represented as java.lang.Integer
     @throws com.intersys.objects.CacheException if any error occured while running the method.
     @see <a href = "http://cache-server:57772/csp/documatic/%25CSP.Documatic.cls?APP=1&PAGE=CLASS&LIBRARY=TPCS&CLASSNAME=Institute.Speciality#%Delete"> Method %Delete</A>
    */
    public static void sys_Delete(com.intersys.objects.Database db, com.intersys.objects.Oid oid, java.lang.Integer concurrency) throws com.intersys.objects.CacheException {
        com.intersys.cache.Dataholder[] args = new com.intersys.cache.Dataholder[2];
        args[0] = new com.intersys.cache.Dataholder(oid);
        args[1] = new com.intersys.cache.Dataholder(concurrency);
        com.intersys.cache.Dataholder res = db.runClassMethod(CACHE_CLASS_NAME, "%Delete", args, com.intersys.objects.Database.RET_PRIM);
        db.parseStatus(res);
        return;
    }

    /**
     <p>Runs method %DeleteExtent in Cache.</p>
     <p>Description: Delete all instances of this class from its extent. On exit <var>instancecount</var> 
contains the original number of instances while <var>deletecount</var> contains 
the number of instances actually deleted.

<p>Internally, <METHOD>%DeleteExtent</METHOD> iterates over the set of instances in the 
collection and invokes the <METHOD>%Delete</METHOD> method.

Refer to <LINK href=/AboutConcurrency.html>About Concurrency</LINK> for more details 
on the optional <var>concurrency</var> argument.

<p>Returns a <CLASS>%Status</CLASS> value indicating success or failure.</p>
     @param db represented as com.intersys.objects.Database
     @param concurrency represented as java.lang.Integer
     @param deletecount represented as com.intersys.objects.StringHolder
     @param instancecount represented as com.intersys.objects.StringHolder
     @throws com.intersys.objects.CacheException if any error occured while running the method.
     @see <a href = "http://cache-server:57772/csp/documatic/%25CSP.Documatic.cls?APP=1&PAGE=CLASS&LIBRARY=TPCS&CLASSNAME=Institute.Speciality#%DeleteExtent"> Method %DeleteExtent</A>
    */
    public static void sys_DeleteExtent(com.intersys.objects.Database db, java.lang.Integer concurrency, com.intersys.objects.StringHolder deletecount, com.intersys.objects.StringHolder instancecount) throws com.intersys.objects.CacheException {
        com.intersys.cache.Dataholder[] args = new com.intersys.cache.Dataholder[3];
        int[] _refs = new int[2];
        args[0] = new com.intersys.cache.Dataholder(concurrency);
        args[1] = com.intersys.cache.Dataholder.create(deletecount.value);
        _refs[0] = 2;
        args[2] = com.intersys.cache.Dataholder.create(instancecount.value);
        _refs[1] = 3;
        com.intersys.cache.Dataholder[] res = db.runClassMethod(CACHE_CLASS_NAME, "%DeleteExtent", _refs, args, com.intersys.objects.Database.RET_PRIM);
        deletecount.set(res[1].getString());
        instancecount.set(res[2].getString());
        db.parseStatus(res[0]);
        return;
    }

    /**
     <p>Runs method %DeleteId in Cache.</p>
     <p>Description: Deletes the stored version of the object with ID <var>id</var> from the database. 

<p><METHOD>%DeleteId</METHOD> is identical in operation to the <METHOD>%Delete</METHOD> method except 
that it uses and Id value instead of an OID value to find an object.

Refer to <LINK href=/AboutConcurrency.html>About Concurrency</LINK> for more details 
on the optional <var>concurrency</var> argument.</p>
     @param db represented as com.intersys.objects.Database
     @param id represented as java.lang.String
     default argument concurrency set to -1
     @throws com.intersys.objects.CacheException if any error occured while running the method.
     @see #sys_DeleteId(com.intersys.objects.Database,java.lang.String,java.lang.Integer)
     @see <a href = "http://cache-server:57772/csp/documatic/%25CSP.Documatic.cls?APP=1&PAGE=CLASS&LIBRARY=TPCS&CLASSNAME=Institute.Speciality#%DeleteId"> Method %DeleteId</A>
    */
    public static void sys_DeleteId(com.intersys.objects.Database db, java.lang.String id) throws com.intersys.objects.CacheException {
        com.intersys.cache.Dataholder[] args = new com.intersys.cache.Dataholder[1];
        args[0] = new com.intersys.cache.Dataholder(id);
        com.intersys.cache.Dataholder res = db.runClassMethod(CACHE_CLASS_NAME, "%DeleteId", args, com.intersys.objects.Database.RET_PRIM);
        db.parseStatus(res);
        return;
    }

    /**
     <p>Runs method %DeleteId in Cache.</p>
     <p>Description: Deletes the stored version of the object with ID <var>id</var> from the database. 

<p><METHOD>%DeleteId</METHOD> is identical in operation to the <METHOD>%Delete</METHOD> method except 
that it uses and Id value instead of an OID value to find an object.

Refer to <LINK href=/AboutConcurrency.html>About Concurrency</LINK> for more details 
on the optional <var>concurrency</var> argument.</p>
     @param db represented as com.intersys.objects.Database
     @param id represented as java.lang.String
     @param concurrency represented as java.lang.Integer
     @throws com.intersys.objects.CacheException if any error occured while running the method.
     @see <a href = "http://cache-server:57772/csp/documatic/%25CSP.Documatic.cls?APP=1&PAGE=CLASS&LIBRARY=TPCS&CLASSNAME=Institute.Speciality#%DeleteId"> Method %DeleteId</A>
    */
    public static void sys_DeleteId(com.intersys.objects.Database db, java.lang.String id, java.lang.Integer concurrency) throws com.intersys.objects.CacheException {
        com.intersys.cache.Dataholder[] args = new com.intersys.cache.Dataholder[2];
        args[0] = new com.intersys.cache.Dataholder(id);
        args[1] = new com.intersys.cache.Dataholder(concurrency);
        com.intersys.cache.Dataholder res = db.runClassMethod(CACHE_CLASS_NAME, "%DeleteId", args, com.intersys.objects.Database.RET_PRIM);
        db.parseStatus(res);
        return;
    }

    /**
     <p>Runs method %Exists in Cache.</p>
     @param db represented as com.intersys.objects.Database
     default argument oid set to ""
     @throws com.intersys.objects.CacheException if any error occured while running the method.
     @see #sys_Exists(com.intersys.objects.Database,com.intersys.objects.Oid)
     @see <a href = "http://cache-server:57772/csp/documatic/%25CSP.Documatic.cls?APP=1&PAGE=CLASS&LIBRARY=TPCS&CLASSNAME=Institute.Speciality#%Exists"> Method %Exists</A>
    */
    public static java.lang.Boolean sys_Exists(com.intersys.objects.Database db) throws com.intersys.objects.CacheException {
        com.intersys.cache.Dataholder[] args = new com.intersys.cache.Dataholder[0];
        com.intersys.cache.Dataholder res = db.runClassMethod(CACHE_CLASS_NAME, "%Exists", args, com.intersys.objects.Database.RET_PRIM);
        return res.getBoolean();
    }

    /**
     <p>Runs method %Exists in Cache.</p>
     @param db represented as com.intersys.objects.Database
     @param oid represented as com.intersys.objects.Oid
     @throws com.intersys.objects.CacheException if any error occured while running the method.
     @see <a href = "http://cache-server:57772/csp/documatic/%25CSP.Documatic.cls?APP=1&PAGE=CLASS&LIBRARY=TPCS&CLASSNAME=Institute.Speciality#%Exists"> Method %Exists</A>
    */
    public static java.lang.Boolean sys_Exists(com.intersys.objects.Database db, com.intersys.objects.Oid oid) throws com.intersys.objects.CacheException {
        com.intersys.cache.Dataholder[] args = new com.intersys.cache.Dataholder[1];
        args[0] = new com.intersys.cache.Dataholder(oid);
        com.intersys.cache.Dataholder res = db.runClassMethod(CACHE_CLASS_NAME, "%Exists", args, com.intersys.objects.Database.RET_PRIM);
        return res.getBoolean();
    }

    /**
     <p>Runs method %ExistsId in Cache.</p>
     @param db represented as com.intersys.objects.Database
     @param id represented as java.lang.String
     @throws com.intersys.objects.CacheException if any error occured while running the method.
     @see <a href = "http://cache-server:57772/csp/documatic/%25CSP.Documatic.cls?APP=1&PAGE=CLASS&LIBRARY=TPCS&CLASSNAME=Institute.Speciality#%ExistsId"> Method %ExistsId</A>
    */
    public static java.lang.Boolean sys_ExistsId(com.intersys.objects.Database db, java.lang.String id) throws com.intersys.objects.CacheException {
        com.intersys.cache.Dataholder[] args = new com.intersys.cache.Dataholder[1];
        args[0] = new com.intersys.cache.Dataholder(id);
        com.intersys.cache.Dataholder res = db.runClassMethod(CACHE_CLASS_NAME, "%ExistsId", args, com.intersys.objects.Database.RET_PRIM);
        return res.getBoolean();
    }

    /**
     <p>Runs method %IsA in Cache.</p>
     <p>Description: Returns true (1) if instances of this class are also instances of the isclass parameter.
That is 'isclass' is a primary superclass of this object.</p>
     @param db represented as com.intersys.objects.Database
     @param isclass represented as java.lang.String
     @throws com.intersys.objects.CacheException if any error occured while running the method.
     @see <a href = "http://cache-server:57772/csp/documatic/%25CSP.Documatic.cls?APP=1&PAGE=CLASS&LIBRARY=TPCS&CLASSNAME=Institute.Speciality#%IsA"> Method %IsA</A>
    */
    public static java.lang.Integer sys_IsA(com.intersys.objects.Database db, java.lang.String isclass) throws com.intersys.objects.CacheException {
        com.intersys.cache.Dataholder[] args = new com.intersys.cache.Dataholder[1];
        args[0] = new com.intersys.cache.Dataholder(isclass);
        com.intersys.cache.Dataholder res = db.runClassMethod(CACHE_CLASS_NAME, "%IsA", args, com.intersys.objects.Database.RET_PRIM);
        return res.getInteger();
    }

    /**
     <p>Runs method %KillExtent in Cache.</p>
     <p>Description: %KillExtent performs a physical kill of the extent. No constraints are enforced. This method should not be
used in place of %DeleteExtent. Only physical storage occupied by this extent will be killed. If this extent
is a subextent of another class then no data is killed. Indexes that originate with this extent will be killed.
%KillExtent will be called on any subextents and on any child extents (the extent of the type class of a
relationship whose cardinality = children is a 'child' extent) of this extent.

This method is not meant for production applications. It is meant to be a development utility to quickly clear
extent physical data.</p>
     @param db represented as com.intersys.objects.Database
     @throws com.intersys.objects.CacheException if any error occured while running the method.
     @see <a href = "http://cache-server:57772/csp/documatic/%25CSP.Documatic.cls?APP=1&PAGE=CLASS&LIBRARY=TPCS&CLASSNAME=Institute.Speciality#%KillExtent"> Method %KillExtent</A>
    */
    public static void sys_KillExtent(com.intersys.objects.Database db) throws com.intersys.objects.CacheException {
        com.intersys.cache.Dataholder[] args = new com.intersys.cache.Dataholder[0];
        com.intersys.cache.Dataholder res = db.runClassMethod(CACHE_CLASS_NAME, "%KillExtent", args, com.intersys.objects.Database.RET_PRIM);
        db.parseStatus(res);
        return;
    }

    /**
     <p>Runs method %SaveIndices in Cache.</p>
     <p>Description: Files the indices for all objects whose ID is in the range defined by <var>pStartId</var> and <var>pEndId</var>.
If <var>pEndId</var> is null then it defaults to <var>pStartId</var>. If <var>pStartId</var> is null then the
range is empty and no filing will occur.

If <var>lockExtent</var> is true then an extent lock will be acquired before the indices
are built. If the lock cannot be acquired then an error is returned. The lock is released
after the index filing is completed.



<p>Returns a <CLASS>%Status</CLASS> value indicating success or failure.</p>
     @param db represented as com.intersys.objects.Database
     default argument pStartId set to ""
     default argument pEndId set to ""
     default argument lockExtent set to 0
     @throws com.intersys.objects.CacheException if any error occured while running the method.
     @see #sys_SaveIndices(com.intersys.objects.Database,java.lang.String,java.lang.String,java.lang.Boolean)
     @see <a href = "http://cache-server:57772/csp/documatic/%25CSP.Documatic.cls?APP=1&PAGE=CLASS&LIBRARY=TPCS&CLASSNAME=Institute.Speciality#%SaveIndices"> Method %SaveIndices</A>
    */
    public static void sys_SaveIndices(com.intersys.objects.Database db) throws com.intersys.objects.CacheException {
        com.intersys.cache.Dataholder[] args = new com.intersys.cache.Dataholder[0];
        com.intersys.cache.Dataholder res = db.runClassMethod(CACHE_CLASS_NAME, "%SaveIndices", args, com.intersys.objects.Database.RET_PRIM);
        db.parseStatus(res);
        return;
    }

    /**
     <p>Runs method %SaveIndices in Cache.</p>
     <p>Description: Files the indices for all objects whose ID is in the range defined by <var>pStartId</var> and <var>pEndId</var>.
If <var>pEndId</var> is null then it defaults to <var>pStartId</var>. If <var>pStartId</var> is null then the
range is empty and no filing will occur.

If <var>lockExtent</var> is true then an extent lock will be acquired before the indices
are built. If the lock cannot be acquired then an error is returned. The lock is released
after the index filing is completed.



<p>Returns a <CLASS>%Status</CLASS> value indicating success or failure.</p>
     @param db represented as com.intersys.objects.Database
     @param pStartId represented as java.lang.String
     default argument pEndId set to ""
     default argument lockExtent set to 0
     @throws com.intersys.objects.CacheException if any error occured while running the method.
     @see #sys_SaveIndices(com.intersys.objects.Database,java.lang.String,java.lang.String,java.lang.Boolean)
     @see <a href = "http://cache-server:57772/csp/documatic/%25CSP.Documatic.cls?APP=1&PAGE=CLASS&LIBRARY=TPCS&CLASSNAME=Institute.Speciality#%SaveIndices"> Method %SaveIndices</A>
    */
    public static void sys_SaveIndices(com.intersys.objects.Database db, java.lang.String pStartId) throws com.intersys.objects.CacheException {
        com.intersys.cache.Dataholder[] args = new com.intersys.cache.Dataholder[1];
        args[0] = new com.intersys.cache.Dataholder(pStartId);
        com.intersys.cache.Dataholder res = db.runClassMethod(CACHE_CLASS_NAME, "%SaveIndices", args, com.intersys.objects.Database.RET_PRIM);
        db.parseStatus(res);
        return;
    }

    /**
     <p>Runs method %SaveIndices in Cache.</p>
     <p>Description: Files the indices for all objects whose ID is in the range defined by <var>pStartId</var> and <var>pEndId</var>.
If <var>pEndId</var> is null then it defaults to <var>pStartId</var>. If <var>pStartId</var> is null then the
range is empty and no filing will occur.

If <var>lockExtent</var> is true then an extent lock will be acquired before the indices
are built. If the lock cannot be acquired then an error is returned. The lock is released
after the index filing is completed.



<p>Returns a <CLASS>%Status</CLASS> value indicating success or failure.</p>
     @param db represented as com.intersys.objects.Database
     @param pStartId represented as java.lang.String
     @param pEndId represented as java.lang.String
     default argument lockExtent set to 0
     @throws com.intersys.objects.CacheException if any error occured while running the method.
     @see #sys_SaveIndices(com.intersys.objects.Database,java.lang.String,java.lang.String,java.lang.Boolean)
     @see <a href = "http://cache-server:57772/csp/documatic/%25CSP.Documatic.cls?APP=1&PAGE=CLASS&LIBRARY=TPCS&CLASSNAME=Institute.Speciality#%SaveIndices"> Method %SaveIndices</A>
    */
    public static void sys_SaveIndices(com.intersys.objects.Database db, java.lang.String pStartId, java.lang.String pEndId) throws com.intersys.objects.CacheException {
        com.intersys.cache.Dataholder[] args = new com.intersys.cache.Dataholder[2];
        args[0] = new com.intersys.cache.Dataholder(pStartId);
        args[1] = new com.intersys.cache.Dataholder(pEndId);
        com.intersys.cache.Dataholder res = db.runClassMethod(CACHE_CLASS_NAME, "%SaveIndices", args, com.intersys.objects.Database.RET_PRIM);
        db.parseStatus(res);
        return;
    }

    /**
     <p>Runs method %SaveIndices in Cache.</p>
     <p>Description: Files the indices for all objects whose ID is in the range defined by <var>pStartId</var> and <var>pEndId</var>.
If <var>pEndId</var> is null then it defaults to <var>pStartId</var>. If <var>pStartId</var> is null then the
range is empty and no filing will occur.

If <var>lockExtent</var> is true then an extent lock will be acquired before the indices
are built. If the lock cannot be acquired then an error is returned. The lock is released
after the index filing is completed.



<p>Returns a <CLASS>%Status</CLASS> value indicating success or failure.</p>
     @param db represented as com.intersys.objects.Database
     @param pStartId represented as java.lang.String
     @param pEndId represented as java.lang.String
     @param lockExtent represented as java.lang.Boolean
     @throws com.intersys.objects.CacheException if any error occured while running the method.
     @see <a href = "http://cache-server:57772/csp/documatic/%25CSP.Documatic.cls?APP=1&PAGE=CLASS&LIBRARY=TPCS&CLASSNAME=Institute.Speciality#%SaveIndices"> Method %SaveIndices</A>
    */
    public static void sys_SaveIndices(com.intersys.objects.Database db, java.lang.String pStartId, java.lang.String pEndId, java.lang.Boolean lockExtent) throws com.intersys.objects.CacheException {
        com.intersys.cache.Dataholder[] args = new com.intersys.cache.Dataholder[3];
        args[0] = new com.intersys.cache.Dataholder(pStartId);
        args[1] = new com.intersys.cache.Dataholder(pEndId);
        args[2] = new com.intersys.cache.Dataholder(lockExtent);
        com.intersys.cache.Dataholder res = db.runClassMethod(CACHE_CLASS_NAME, "%SaveIndices", args, com.intersys.objects.Database.RET_PRIM);
        db.parseStatus(res);
        return;
    }

    /**
     <p>Runs method GroupsGetObject in Cache.</p>
     default argument force set to 0
     @throws com.intersys.objects.CacheException if any error occured while running the method.
     @see #GroupsGetObject(java.lang.Integer)
     @see <a href = "http://cache-server:57772/csp/documatic/%25CSP.Documatic.cls?APP=1&PAGE=CLASS&LIBRARY=TPCS&CLASSNAME=Institute.Speciality#GroupsGetObject"> Method GroupsGetObject</A>
    */
    public com.intersys.objects.Oid GroupsGetObject() throws com.intersys.objects.CacheException {
        com.intersys.cache.Dataholder[] args = new com.intersys.cache.Dataholder[0];
        com.intersys.cache.Dataholder res = mInternal.runInstanceMethod("GroupsGetObject", args, com.intersys.objects.Database.RET_PRIM);
        return res.getOid();
    }

    /**
     <p>Runs method GroupsGetObject in Cache.</p>
     @param force represented as java.lang.Integer
     @throws com.intersys.objects.CacheException if any error occured while running the method.
     @see <a href = "http://cache-server:57772/csp/documatic/%25CSP.Documatic.cls?APP=1&PAGE=CLASS&LIBRARY=TPCS&CLASSNAME=Institute.Speciality#GroupsGetObject"> Method GroupsGetObject</A>
    */
    public com.intersys.objects.Oid GroupsGetObject(java.lang.Integer force) throws com.intersys.objects.CacheException {
        com.intersys.cache.Dataholder[] args = new com.intersys.cache.Dataholder[1];
        args[0] = new com.intersys.cache.Dataholder(force);
        com.intersys.cache.Dataholder res = mInternal.runInstanceMethod("GroupsGetObject", args, com.intersys.objects.Database.RET_PRIM);
        return res.getOid();
    }

    /**
     <p>Runs method GroupsGetObjectId in Cache.</p>
     default argument force set to 0
     @throws com.intersys.objects.CacheException if any error occured while running the method.
     @see #GroupsGetObjectId(java.lang.Integer)
     @see <a href = "http://cache-server:57772/csp/documatic/%25CSP.Documatic.cls?APP=1&PAGE=CLASS&LIBRARY=TPCS&CLASSNAME=Institute.Speciality#GroupsGetObjectId"> Method GroupsGetObjectId</A>
    */
    public java.lang.String GroupsGetObjectId() throws com.intersys.objects.CacheException {
        com.intersys.cache.Dataholder[] args = new com.intersys.cache.Dataholder[0];
        com.intersys.cache.Dataholder res = mInternal.runInstanceMethod("GroupsGetObjectId", args, com.intersys.objects.Database.RET_PRIM);
        return res.getString();
    }

    /**
     <p>Runs method GroupsGetObjectId in Cache.</p>
     @param force represented as java.lang.Integer
     @throws com.intersys.objects.CacheException if any error occured while running the method.
     @see <a href = "http://cache-server:57772/csp/documatic/%25CSP.Documatic.cls?APP=1&PAGE=CLASS&LIBRARY=TPCS&CLASSNAME=Institute.Speciality#GroupsGetObjectId"> Method GroupsGetObjectId</A>
    */
    public java.lang.String GroupsGetObjectId(java.lang.Integer force) throws com.intersys.objects.CacheException {
        com.intersys.cache.Dataholder[] args = new com.intersys.cache.Dataholder[1];
        args[0] = new com.intersys.cache.Dataholder(force);
        com.intersys.cache.Dataholder res = mInternal.runInstanceMethod("GroupsGetObjectId", args, com.intersys.objects.Database.RET_PRIM);
        return res.getString();
    }

    /**
     <p>Runs method GroupsIsValid in Cache.</p>
     @param db represented as com.intersys.objects.Database
     @param value represented as java.lang.String
     @throws com.intersys.objects.CacheException if any error occured while running the method.
     @see <a href = "http://cache-server:57772/csp/documatic/%25CSP.Documatic.cls?APP=1&PAGE=CLASS&LIBRARY=TPCS&CLASSNAME=Institute.Speciality#GroupsIsValid"> Method GroupsIsValid</A>
    */
    public static void GroupsIsValid(com.intersys.objects.Database db, java.lang.String value) throws com.intersys.objects.CacheException {
        com.intersys.cache.Dataholder[] args = new com.intersys.cache.Dataholder[1];
        args[0] = new com.intersys.cache.Dataholder(value);
        com.intersys.cache.Dataholder res = db.runClassMethod(CACHE_CLASS_NAME, "GroupsIsValid", args, com.intersys.objects.Database.RET_PRIM);
        db.parseStatus(res);
        return;
    }

    /**
     <p>Runs method IDKEYDelete in Cache.</p>
     @param db represented as com.intersys.objects.Database
     @param K1 represented as java.lang.String
     default argument concurrency set to -1
     @throws com.intersys.objects.CacheException if any error occured while running the method.
     @see #IDKEYDelete(com.intersys.objects.Database,java.lang.String,java.lang.Integer)
     @see <a href = "http://cache-server:57772/csp/documatic/%25CSP.Documatic.cls?APP=1&PAGE=CLASS&LIBRARY=TPCS&CLASSNAME=Institute.Speciality#IDKEYDelete"> Method IDKEYDelete</A>
    */
    public static void IDKEYDelete(com.intersys.objects.Database db, java.lang.String K1) throws com.intersys.objects.CacheException {
        com.intersys.cache.Dataholder[] args = new com.intersys.cache.Dataholder[1];
        args[0] = new com.intersys.cache.Dataholder(K1);
        com.intersys.cache.Dataholder res = db.runClassMethod(CACHE_CLASS_NAME, "IDKEYDelete", args, com.intersys.objects.Database.RET_PRIM);
        db.parseStatus(res);
        return;
    }

    /**
     <p>Runs method IDKEYDelete in Cache.</p>
     @param db represented as com.intersys.objects.Database
     @param K1 represented as java.lang.String
     @param concurrency represented as java.lang.Integer
     @throws com.intersys.objects.CacheException if any error occured while running the method.
     @see <a href = "http://cache-server:57772/csp/documatic/%25CSP.Documatic.cls?APP=1&PAGE=CLASS&LIBRARY=TPCS&CLASSNAME=Institute.Speciality#IDKEYDelete"> Method IDKEYDelete</A>
    */
    public static void IDKEYDelete(com.intersys.objects.Database db, java.lang.String K1, java.lang.Integer concurrency) throws com.intersys.objects.CacheException {
        com.intersys.cache.Dataholder[] args = new com.intersys.cache.Dataholder[2];
        args[0] = new com.intersys.cache.Dataholder(K1);
        args[1] = new com.intersys.cache.Dataholder(concurrency);
        com.intersys.cache.Dataholder res = db.runClassMethod(CACHE_CLASS_NAME, "IDKEYDelete", args, com.intersys.objects.Database.RET_PRIM);
        db.parseStatus(res);
        return;
    }

    /**
     <p>Runs method IDKEYExists in Cache.</p>
     @param db represented as com.intersys.objects.Database
     @param K1 represented as java.lang.String
     default argument id set to ""
     @throws com.intersys.objects.CacheException if any error occured while running the method.
     @see #IDKEYExists(com.intersys.objects.Database,java.lang.String,com.intersys.objects.StringHolder)
     @see <a href = "http://cache-server:57772/csp/documatic/%25CSP.Documatic.cls?APP=1&PAGE=CLASS&LIBRARY=TPCS&CLASSNAME=Institute.Speciality#IDKEYExists"> Method IDKEYExists</A>
    */
    public static java.lang.Boolean IDKEYExists(com.intersys.objects.Database db, java.lang.String K1) throws com.intersys.objects.CacheException {
        com.intersys.cache.Dataholder[] args = new com.intersys.cache.Dataholder[1];
        args[0] = new com.intersys.cache.Dataholder(K1);
        com.intersys.cache.Dataholder res = db.runClassMethod(CACHE_CLASS_NAME, "IDKEYExists", args, com.intersys.objects.Database.RET_PRIM);
        return res.getBoolean();
    }

    /**
     <p>Runs method IDKEYExists in Cache.</p>
     @param db represented as com.intersys.objects.Database
     @param K1 represented as java.lang.String
     @param id represented as com.intersys.objects.StringHolder
     @throws com.intersys.objects.CacheException if any error occured while running the method.
     @see <a href = "http://cache-server:57772/csp/documatic/%25CSP.Documatic.cls?APP=1&PAGE=CLASS&LIBRARY=TPCS&CLASSNAME=Institute.Speciality#IDKEYExists"> Method IDKEYExists</A>
    */
    public static java.lang.Boolean IDKEYExists(com.intersys.objects.Database db, java.lang.String K1, com.intersys.objects.StringHolder id) throws com.intersys.objects.CacheException {
        com.intersys.cache.Dataholder[] args = new com.intersys.cache.Dataholder[2];
        int[] _refs = new int[1];
        args[0] = new com.intersys.cache.Dataholder(K1);
        args[1] = com.intersys.cache.Dataholder.create(id.value);
        _refs[0] = 2;
        com.intersys.cache.Dataholder[] res = db.runClassMethod(CACHE_CLASS_NAME, "IDKEYExists", _refs, args, com.intersys.objects.Database.RET_PRIM);
        id.set(res[1].getString());
        return res[0].getBoolean();
    }

    /**
     <p>Runs method IDKEYOpen in Cache.</p>
     @param db represented as com.intersys.objects.Database
     @param K1 represented as java.lang.String
     default argument concurrency set to -1
     default argument sc set to $$$OK
     @throws com.intersys.objects.CacheException if any error occured while running the method.
     @see #IDKEYOpen(com.intersys.objects.Database,java.lang.String,java.lang.Integer,com.intersys.objects.StatusCodeHolder)
     @see <a href = "http://cache-server:57772/csp/documatic/%25CSP.Documatic.cls?APP=1&PAGE=CLASS&LIBRARY=TPCS&CLASSNAME=Institute.Speciality#IDKEYOpen"> Method IDKEYOpen</A>
    */
    public static Institute.Speciality IDKEYOpen(com.intersys.objects.Database db, java.lang.String K1) throws com.intersys.objects.CacheException {
        com.intersys.cache.Dataholder[] args = new com.intersys.cache.Dataholder[1];
        args[0] = new com.intersys.cache.Dataholder(K1);
        com.intersys.cache.Dataholder res = db.runClassMethod(CACHE_CLASS_NAME, "IDKEYOpen", args, com.intersys.objects.Database.RET_OBJECT);
        com.intersys.cache.CacheObject cobj = res.getCacheObject();
        if (cobj == null) return null;
        return (Institute.Speciality) (cobj.newJavaInstance());
    }

    /**
     <p>Runs method IDKEYOpen in Cache.</p>
     @param db represented as com.intersys.objects.Database
     @param K1 represented as java.lang.String
     @param concurrency represented as java.lang.Integer
     default argument sc set to $$$OK
     @throws com.intersys.objects.CacheException if any error occured while running the method.
     @see #IDKEYOpen(com.intersys.objects.Database,java.lang.String,java.lang.Integer,com.intersys.objects.StatusCodeHolder)
     @see <a href = "http://cache-server:57772/csp/documatic/%25CSP.Documatic.cls?APP=1&PAGE=CLASS&LIBRARY=TPCS&CLASSNAME=Institute.Speciality#IDKEYOpen"> Method IDKEYOpen</A>
    */
    public static Institute.Speciality IDKEYOpen(com.intersys.objects.Database db, java.lang.String K1, java.lang.Integer concurrency) throws com.intersys.objects.CacheException {
        com.intersys.cache.Dataholder[] args = new com.intersys.cache.Dataholder[2];
        args[0] = new com.intersys.cache.Dataholder(K1);
        args[1] = new com.intersys.cache.Dataholder(concurrency);
        com.intersys.cache.Dataholder res = db.runClassMethod(CACHE_CLASS_NAME, "IDKEYOpen", args, com.intersys.objects.Database.RET_OBJECT);
        com.intersys.cache.CacheObject cobj = res.getCacheObject();
        if (cobj == null) return null;
        return (Institute.Speciality) (cobj.newJavaInstance());
    }

    /**
     <p>Runs method IDKEYOpen in Cache.</p>
     @param db represented as com.intersys.objects.Database
     @param K1 represented as java.lang.String
     @param concurrency represented as java.lang.Integer
     @param sc represented as com.intersys.objects.StatusCodeHolder
     @throws com.intersys.objects.CacheException if any error occured while running the method.
     @see <a href = "http://cache-server:57772/csp/documatic/%25CSP.Documatic.cls?APP=1&PAGE=CLASS&LIBRARY=TPCS&CLASSNAME=Institute.Speciality#IDKEYOpen"> Method IDKEYOpen</A>
    */
    public static Institute.Speciality IDKEYOpen(com.intersys.objects.Database db, java.lang.String K1, java.lang.Integer concurrency, com.intersys.objects.StatusCodeHolder sc) throws com.intersys.objects.CacheException {
        com.intersys.cache.Dataholder[] args = new com.intersys.cache.Dataholder[3];
        int[] _refs = new int[1];
        args[0] = new com.intersys.cache.Dataholder(K1);
        args[1] = new com.intersys.cache.Dataholder(concurrency);
        args[2] = com.intersys.cache.Dataholder.create(sc.value);
        _refs[0] = 3;
        com.intersys.cache.Dataholder[] res = db.runClassMethod(CACHE_CLASS_NAME, "IDKEYOpen", _refs, args, com.intersys.objects.Database.RET_OBJECT);
        sc.set(res[1].getStatusCode());
        com.intersys.cache.CacheObject cobj = res[0].getCacheObject();
        if (cobj == null) return null;
        return (Institute.Speciality) (cobj.newJavaInstance());
    }

    /**
     <p>Runs method NameDisplayToLogical in Cache.</p>
     @param db represented as com.intersys.objects.Database
     @param _val represented as java.lang.String
     @throws com.intersys.objects.CacheException if any error occured while running the method.
     @see <a href = "http://cache-server:57772/csp/documatic/%25CSP.Documatic.cls?APP=1&PAGE=CLASS&LIBRARY=TPCS&CLASSNAME=Institute.Speciality#NameDisplayToLogical"> Method NameDisplayToLogical</A>
    */
    public static java.lang.String NameDisplayToLogical(com.intersys.objects.Database db, java.lang.String _val) throws com.intersys.objects.CacheException {
        com.intersys.cache.Dataholder[] args = new com.intersys.cache.Dataholder[1];
        args[0] = new com.intersys.cache.Dataholder(_val);
        com.intersys.cache.Dataholder res = db.runClassMethod(CACHE_CLASS_NAME, "NameDisplayToLogical", args, com.intersys.objects.Database.RET_PRIM);
        return res.getString();
    }

    /**
     <p>Runs method NameIsValid in Cache.</p>
     @param db represented as com.intersys.objects.Database
     @param _val represented as java.lang.String
     @throws com.intersys.objects.CacheException if any error occured while running the method.
     @see <a href = "http://cache-server:57772/csp/documatic/%25CSP.Documatic.cls?APP=1&PAGE=CLASS&LIBRARY=TPCS&CLASSNAME=Institute.Speciality#NameIsValid"> Method NameIsValid</A>
    */
    public static void NameIsValid(com.intersys.objects.Database db, java.lang.String _val) throws com.intersys.objects.CacheException {
        com.intersys.cache.Dataholder[] args = new com.intersys.cache.Dataholder[1];
        args[0] = new com.intersys.cache.Dataholder(_val);
        com.intersys.cache.Dataholder res = db.runClassMethod(CACHE_CLASS_NAME, "NameIsValid", args, com.intersys.objects.Database.RET_PRIM);
        db.parseStatus(res);
        return;
    }

    /**
     <p>Runs method NameLogicalToDisplay in Cache.</p>
     @param db represented as com.intersys.objects.Database
     @param _val represented as java.lang.String
     @throws com.intersys.objects.CacheException if any error occured while running the method.
     @see <a href = "http://cache-server:57772/csp/documatic/%25CSP.Documatic.cls?APP=1&PAGE=CLASS&LIBRARY=TPCS&CLASSNAME=Institute.Speciality#NameLogicalToDisplay"> Method NameLogicalToDisplay</A>
    */
    public static java.lang.String NameLogicalToDisplay(com.intersys.objects.Database db, java.lang.String _val) throws com.intersys.objects.CacheException {
        com.intersys.cache.Dataholder[] args = new com.intersys.cache.Dataholder[1];
        args[0] = new com.intersys.cache.Dataholder(_val);
        com.intersys.cache.Dataholder res = db.runClassMethod(CACHE_CLASS_NAME, "NameLogicalToDisplay", args, com.intersys.objects.Database.RET_PRIM);
        return res.getString();
    }

    /**
     <p>Runs method NumberDisplayToLogical in Cache.</p>
     @param db represented as com.intersys.objects.Database
     @param _val represented as java.lang.String
     @throws com.intersys.objects.CacheException if any error occured while running the method.
     @see <a href = "http://cache-server:57772/csp/documatic/%25CSP.Documatic.cls?APP=1&PAGE=CLASS&LIBRARY=TPCS&CLASSNAME=Institute.Speciality#NumberDisplayToLogical"> Method NumberDisplayToLogical</A>
    */
    public static java.lang.String NumberDisplayToLogical(com.intersys.objects.Database db, java.lang.String _val) throws com.intersys.objects.CacheException {
        com.intersys.cache.Dataholder[] args = new com.intersys.cache.Dataholder[1];
        args[0] = new com.intersys.cache.Dataholder(_val);
        com.intersys.cache.Dataholder res = db.runClassMethod(CACHE_CLASS_NAME, "NumberDisplayToLogical", args, com.intersys.objects.Database.RET_PRIM);
        return res.getString();
    }

    /**
     <p>Runs method NumberIsValid in Cache.</p>
     @param db represented as com.intersys.objects.Database
     @param _val represented as java.lang.String
     @throws com.intersys.objects.CacheException if any error occured while running the method.
     @see <a href = "http://cache-server:57772/csp/documatic/%25CSP.Documatic.cls?APP=1&PAGE=CLASS&LIBRARY=TPCS&CLASSNAME=Institute.Speciality#NumberIsValid"> Method NumberIsValid</A>
    */
    public static void NumberIsValid(com.intersys.objects.Database db, java.lang.String _val) throws com.intersys.objects.CacheException {
        com.intersys.cache.Dataholder[] args = new com.intersys.cache.Dataholder[1];
        args[0] = new com.intersys.cache.Dataholder(_val);
        com.intersys.cache.Dataholder res = db.runClassMethod(CACHE_CLASS_NAME, "NumberIsValid", args, com.intersys.objects.Database.RET_PRIM);
        db.parseStatus(res);
        return;
    }

    /**
     <p>Runs method NumberLogicalToDisplay in Cache.</p>
     @param db represented as com.intersys.objects.Database
     @param _val represented as java.lang.String
     @throws com.intersys.objects.CacheException if any error occured while running the method.
     @see <a href = "http://cache-server:57772/csp/documatic/%25CSP.Documatic.cls?APP=1&PAGE=CLASS&LIBRARY=TPCS&CLASSNAME=Institute.Speciality#NumberLogicalToDisplay"> Method NumberLogicalToDisplay</A>
    */
    public static java.lang.String NumberLogicalToDisplay(com.intersys.objects.Database db, java.lang.String _val) throws com.intersys.objects.CacheException {
        com.intersys.cache.Dataholder[] args = new com.intersys.cache.Dataholder[1];
        args[0] = new com.intersys.cache.Dataholder(_val);
        com.intersys.cache.Dataholder res = db.runClassMethod(CACHE_CLASS_NAME, "NumberLogicalToDisplay", args, com.intersys.objects.Database.RET_PRIM);
        return res.getString();
    }

    /**
     <p>Runs method Populate in Cache.</p>
     <p>Description: Creates up to <var>count</var> instances an object and stores 
them in the database.

<p>If <var>verbose</var> is true, then details are echoed to the console.
<p>If <var>DeferIndices</var> is true, then indices are sorted at the end of the operation.
<p>If <var>objects</var> is true, then each object that is created is returned in the objects array
<p>If <var>tune</var> is true, then TuneTable^%apiSQL is called after the instances of the class have been created.

<P>Returns the number of instances successfully created.

<p>For more information refer to 
<LINK href=DocBook.UI.Page.cls?KEY=GOBJ_populate>The Cach&eacute; Populate Utility</LINK>.</p>
     @param db represented as com.intersys.objects.Database
     default argument count set to 10
     default argument verbose set to 0
     default argument DeferIndices set to 1
     default argument objects set to 0
     default argument tune set to 1
     @throws com.intersys.objects.CacheException if any error occured while running the method.
     @see #Populate(com.intersys.objects.Database,java.lang.Integer,java.lang.Integer,java.lang.Integer,com.intersys.objects.IntegerHolder,java.lang.Integer)
     @see <a href = "http://cache-server:57772/csp/documatic/%25CSP.Documatic.cls?APP=1&PAGE=CLASS&LIBRARY=TPCS&CLASSNAME=Institute.Speciality#Populate"> Method Populate</A>
    */
    public static java.lang.Integer Populate(com.intersys.objects.Database db) throws com.intersys.objects.CacheException {
        com.intersys.cache.Dataholder[] args = new com.intersys.cache.Dataholder[0];
        com.intersys.cache.Dataholder res = db.runClassMethod(CACHE_CLASS_NAME, "Populate", args, com.intersys.objects.Database.RET_PRIM);
        return res.getInteger();
    }

    /**
     <p>Runs method Populate in Cache.</p>
     <p>Description: Creates up to <var>count</var> instances an object and stores 
them in the database.

<p>If <var>verbose</var> is true, then details are echoed to the console.
<p>If <var>DeferIndices</var> is true, then indices are sorted at the end of the operation.
<p>If <var>objects</var> is true, then each object that is created is returned in the objects array
<p>If <var>tune</var> is true, then TuneTable^%apiSQL is called after the instances of the class have been created.

<P>Returns the number of instances successfully created.

<p>For more information refer to 
<LINK href=DocBook.UI.Page.cls?KEY=GOBJ_populate>The Cach&eacute; Populate Utility</LINK>.</p>
     @param db represented as com.intersys.objects.Database
     @param count represented as java.lang.Integer
     default argument verbose set to 0
     default argument DeferIndices set to 1
     default argument objects set to 0
     default argument tune set to 1
     @throws com.intersys.objects.CacheException if any error occured while running the method.
     @see #Populate(com.intersys.objects.Database,java.lang.Integer,java.lang.Integer,java.lang.Integer,com.intersys.objects.IntegerHolder,java.lang.Integer)
     @see <a href = "http://cache-server:57772/csp/documatic/%25CSP.Documatic.cls?APP=1&PAGE=CLASS&LIBRARY=TPCS&CLASSNAME=Institute.Speciality#Populate"> Method Populate</A>
    */
    public static java.lang.Integer Populate(com.intersys.objects.Database db, java.lang.Integer count) throws com.intersys.objects.CacheException {
        com.intersys.cache.Dataholder[] args = new com.intersys.cache.Dataholder[1];
        args[0] = new com.intersys.cache.Dataholder(count);
        com.intersys.cache.Dataholder res = db.runClassMethod(CACHE_CLASS_NAME, "Populate", args, com.intersys.objects.Database.RET_PRIM);
        return res.getInteger();
    }

    /**
     <p>Runs method Populate in Cache.</p>
     <p>Description: Creates up to <var>count</var> instances an object and stores 
them in the database.

<p>If <var>verbose</var> is true, then details are echoed to the console.
<p>If <var>DeferIndices</var> is true, then indices are sorted at the end of the operation.
<p>If <var>objects</var> is true, then each object that is created is returned in the objects array
<p>If <var>tune</var> is true, then TuneTable^%apiSQL is called after the instances of the class have been created.

<P>Returns the number of instances successfully created.

<p>For more information refer to 
<LINK href=DocBook.UI.Page.cls?KEY=GOBJ_populate>The Cach&eacute; Populate Utility</LINK>.</p>
     @param db represented as com.intersys.objects.Database
     @param count represented as java.lang.Integer
     @param verbose represented as java.lang.Integer
     default argument DeferIndices set to 1
     default argument objects set to 0
     default argument tune set to 1
     @throws com.intersys.objects.CacheException if any error occured while running the method.
     @see #Populate(com.intersys.objects.Database,java.lang.Integer,java.lang.Integer,java.lang.Integer,com.intersys.objects.IntegerHolder,java.lang.Integer)
     @see <a href = "http://cache-server:57772/csp/documatic/%25CSP.Documatic.cls?APP=1&PAGE=CLASS&LIBRARY=TPCS&CLASSNAME=Institute.Speciality#Populate"> Method Populate</A>
    */
    public static java.lang.Integer Populate(com.intersys.objects.Database db, java.lang.Integer count, java.lang.Integer verbose) throws com.intersys.objects.CacheException {
        com.intersys.cache.Dataholder[] args = new com.intersys.cache.Dataholder[2];
        args[0] = new com.intersys.cache.Dataholder(count);
        args[1] = new com.intersys.cache.Dataholder(verbose);
        com.intersys.cache.Dataholder res = db.runClassMethod(CACHE_CLASS_NAME, "Populate", args, com.intersys.objects.Database.RET_PRIM);
        return res.getInteger();
    }

    /**
     <p>Runs method Populate in Cache.</p>
     <p>Description: Creates up to <var>count</var> instances an object and stores 
them in the database.

<p>If <var>verbose</var> is true, then details are echoed to the console.
<p>If <var>DeferIndices</var> is true, then indices are sorted at the end of the operation.
<p>If <var>objects</var> is true, then each object that is created is returned in the objects array
<p>If <var>tune</var> is true, then TuneTable^%apiSQL is called after the instances of the class have been created.

<P>Returns the number of instances successfully created.

<p>For more information refer to 
<LINK href=DocBook.UI.Page.cls?KEY=GOBJ_populate>The Cach&eacute; Populate Utility</LINK>.</p>
     @param db represented as com.intersys.objects.Database
     @param count represented as java.lang.Integer
     @param verbose represented as java.lang.Integer
     @param DeferIndices represented as java.lang.Integer
     default argument objects set to 0
     default argument tune set to 1
     @throws com.intersys.objects.CacheException if any error occured while running the method.
     @see #Populate(com.intersys.objects.Database,java.lang.Integer,java.lang.Integer,java.lang.Integer,com.intersys.objects.IntegerHolder,java.lang.Integer)
     @see <a href = "http://cache-server:57772/csp/documatic/%25CSP.Documatic.cls?APP=1&PAGE=CLASS&LIBRARY=TPCS&CLASSNAME=Institute.Speciality#Populate"> Method Populate</A>
    */
    public static java.lang.Integer Populate(com.intersys.objects.Database db, java.lang.Integer count, java.lang.Integer verbose, java.lang.Integer DeferIndices) throws com.intersys.objects.CacheException {
        com.intersys.cache.Dataholder[] args = new com.intersys.cache.Dataholder[3];
        args[0] = new com.intersys.cache.Dataholder(count);
        args[1] = new com.intersys.cache.Dataholder(verbose);
        args[2] = new com.intersys.cache.Dataholder(DeferIndices);
        com.intersys.cache.Dataholder res = db.runClassMethod(CACHE_CLASS_NAME, "Populate", args, com.intersys.objects.Database.RET_PRIM);
        return res.getInteger();
    }

    /**
     <p>Runs method Populate in Cache.</p>
     <p>Description: Creates up to <var>count</var> instances an object and stores 
them in the database.

<p>If <var>verbose</var> is true, then details are echoed to the console.
<p>If <var>DeferIndices</var> is true, then indices are sorted at the end of the operation.
<p>If <var>objects</var> is true, then each object that is created is returned in the objects array
<p>If <var>tune</var> is true, then TuneTable^%apiSQL is called after the instances of the class have been created.

<P>Returns the number of instances successfully created.

<p>For more information refer to 
<LINK href=DocBook.UI.Page.cls?KEY=GOBJ_populate>The Cach&eacute; Populate Utility</LINK>.</p>
     @param db represented as com.intersys.objects.Database
     @param count represented as java.lang.Integer
     @param verbose represented as java.lang.Integer
     @param DeferIndices represented as java.lang.Integer
     @param objects represented as com.intersys.objects.IntegerHolder
     default argument tune set to 1
     @throws com.intersys.objects.CacheException if any error occured while running the method.
     @see #Populate(com.intersys.objects.Database,java.lang.Integer,java.lang.Integer,java.lang.Integer,com.intersys.objects.IntegerHolder,java.lang.Integer)
     @see <a href = "http://cache-server:57772/csp/documatic/%25CSP.Documatic.cls?APP=1&PAGE=CLASS&LIBRARY=TPCS&CLASSNAME=Institute.Speciality#Populate"> Method Populate</A>
    */
    public static java.lang.Integer Populate(com.intersys.objects.Database db, java.lang.Integer count, java.lang.Integer verbose, java.lang.Integer DeferIndices, com.intersys.objects.IntegerHolder objects) throws com.intersys.objects.CacheException {
        com.intersys.cache.Dataholder[] args = new com.intersys.cache.Dataholder[4];
        int[] _refs = new int[1];
        args[0] = new com.intersys.cache.Dataholder(count);
        args[1] = new com.intersys.cache.Dataholder(verbose);
        args[2] = new com.intersys.cache.Dataholder(DeferIndices);
        args[3] = com.intersys.cache.Dataholder.create(objects.value);
        _refs[0] = 4;
        com.intersys.cache.Dataholder[] res = db.runClassMethod(CACHE_CLASS_NAME, "Populate", _refs, args, com.intersys.objects.Database.RET_PRIM);
        objects.set(res[1].getInteger());
        return res[0].getInteger();
    }

    /**
     <p>Runs method Populate in Cache.</p>
     <p>Description: Creates up to <var>count</var> instances an object and stores 
them in the database.

<p>If <var>verbose</var> is true, then details are echoed to the console.
<p>If <var>DeferIndices</var> is true, then indices are sorted at the end of the operation.
<p>If <var>objects</var> is true, then each object that is created is returned in the objects array
<p>If <var>tune</var> is true, then TuneTable^%apiSQL is called after the instances of the class have been created.

<P>Returns the number of instances successfully created.

<p>For more information refer to 
<LINK href=DocBook.UI.Page.cls?KEY=GOBJ_populate>The Cach&eacute; Populate Utility</LINK>.</p>
     @param db represented as com.intersys.objects.Database
     @param count represented as java.lang.Integer
     @param verbose represented as java.lang.Integer
     @param DeferIndices represented as java.lang.Integer
     @param objects represented as com.intersys.objects.IntegerHolder
     @param tune represented as java.lang.Integer
     @throws com.intersys.objects.CacheException if any error occured while running the method.
     @see <a href = "http://cache-server:57772/csp/documatic/%25CSP.Documatic.cls?APP=1&PAGE=CLASS&LIBRARY=TPCS&CLASSNAME=Institute.Speciality#Populate"> Method Populate</A>
    */
    public static java.lang.Integer Populate(com.intersys.objects.Database db, java.lang.Integer count, java.lang.Integer verbose, java.lang.Integer DeferIndices, com.intersys.objects.IntegerHolder objects, java.lang.Integer tune) throws com.intersys.objects.CacheException {
        com.intersys.cache.Dataholder[] args = new com.intersys.cache.Dataholder[5];
        int[] _refs = new int[1];
        args[0] = new com.intersys.cache.Dataholder(count);
        args[1] = new com.intersys.cache.Dataholder(verbose);
        args[2] = new com.intersys.cache.Dataholder(DeferIndices);
        args[3] = com.intersys.cache.Dataholder.create(objects.value);
        _refs[0] = 4;
        args[4] = new com.intersys.cache.Dataholder(tune);
        com.intersys.cache.Dataholder[] res = db.runClassMethod(CACHE_CLASS_NAME, "Populate", _refs, args, com.intersys.objects.Database.RET_PRIM);
        objects.set(res[1].getInteger());
        return res[0].getInteger();
    }

    /**
     <p>Runs method PopulateSerial in Cache.</p>
     <p>Description: Create a single instance of a serial object.</p>
     @param db represented as com.intersys.objects.Database
     @throws com.intersys.objects.CacheException if any error occured while running the method.
     @see <a href = "http://cache-server:57772/csp/documatic/%25CSP.Documatic.cls?APP=1&PAGE=CLASS&LIBRARY=TPCS&CLASSNAME=Institute.Speciality#PopulateSerial"> Method PopulateSerial</A>
    */
    public static java.lang.String PopulateSerial(com.intersys.objects.Database db) throws com.intersys.objects.CacheException {
        com.intersys.cache.Dataholder[] args = new com.intersys.cache.Dataholder[0];
        com.intersys.cache.Dataholder res = db.runClassMethod(CACHE_CLASS_NAME, "PopulateSerial", args, com.intersys.objects.Database.RET_PRIM);
        return res.getString();
    }

    /**
     <p>Runs method SemesterDisplayToLogical in Cache.</p>
     @param db represented as com.intersys.objects.Database
     @param _val represented as java.lang.String
     @throws com.intersys.objects.CacheException if any error occured while running the method.
     @see <a href = "http://cache-server:57772/csp/documatic/%25CSP.Documatic.cls?APP=1&PAGE=CLASS&LIBRARY=TPCS&CLASSNAME=Institute.Speciality#SemesterDisplayToLogical"> Method SemesterDisplayToLogical</A>
    */
    public static java.lang.Integer SemesterDisplayToLogical(com.intersys.objects.Database db, java.lang.String _val) throws com.intersys.objects.CacheException {
        com.intersys.cache.Dataholder[] args = new com.intersys.cache.Dataholder[1];
        args[0] = new com.intersys.cache.Dataholder(_val);
        com.intersys.cache.Dataholder res = db.runClassMethod(CACHE_CLASS_NAME, "SemesterDisplayToLogical", args, com.intersys.objects.Database.RET_PRIM);
        return res.getInteger();
    }

    /**
     <p>Runs method SemesterIsValid in Cache.</p>
     @param db represented as com.intersys.objects.Database
     @param _val represented as java.lang.String
     @throws com.intersys.objects.CacheException if any error occured while running the method.
     @see <a href = "http://cache-server:57772/csp/documatic/%25CSP.Documatic.cls?APP=1&PAGE=CLASS&LIBRARY=TPCS&CLASSNAME=Institute.Speciality#SemesterIsValid"> Method SemesterIsValid</A>
    */
    public static void SemesterIsValid(com.intersys.objects.Database db, java.lang.String _val) throws com.intersys.objects.CacheException {
        com.intersys.cache.Dataholder[] args = new com.intersys.cache.Dataholder[1];
        args[0] = new com.intersys.cache.Dataholder(_val);
        com.intersys.cache.Dataholder res = db.runClassMethod(CACHE_CLASS_NAME, "SemesterIsValid", args, com.intersys.objects.Database.RET_PRIM);
        db.parseStatus(res);
        return;
    }

    /**
     <p>Runs method SemesterLogicalToDisplay in Cache.</p>
     @param db represented as com.intersys.objects.Database
     @param _val represented as java.lang.Integer
     @throws com.intersys.objects.CacheException if any error occured while running the method.
     @see <a href = "http://cache-server:57772/csp/documatic/%25CSP.Documatic.cls?APP=1&PAGE=CLASS&LIBRARY=TPCS&CLASSNAME=Institute.Speciality#SemesterLogicalToDisplay"> Method SemesterLogicalToDisplay</A>
    */
    public static java.lang.String SemesterLogicalToDisplay(com.intersys.objects.Database db, java.lang.Integer _val) throws com.intersys.objects.CacheException {
        com.intersys.cache.Dataholder[] args = new com.intersys.cache.Dataholder[1];
        args[0] = new com.intersys.cache.Dataholder(_val);
        com.intersys.cache.Dataholder res = db.runClassMethod(CACHE_CLASS_NAME, "SemesterLogicalToDisplay", args, com.intersys.objects.Database.RET_PRIM);
        return res.getString();
    }

    /**
     <p>Runs method SubjectsGetObject in Cache.</p>
     default argument force set to 0
     @throws com.intersys.objects.CacheException if any error occured while running the method.
     @see #SubjectsGetObject(java.lang.Integer)
     @see <a href = "http://cache-server:57772/csp/documatic/%25CSP.Documatic.cls?APP=1&PAGE=CLASS&LIBRARY=TPCS&CLASSNAME=Institute.Speciality#SubjectsGetObject"> Method SubjectsGetObject</A>
    */
    public com.intersys.objects.Oid SubjectsGetObject() throws com.intersys.objects.CacheException {
        com.intersys.cache.Dataholder[] args = new com.intersys.cache.Dataholder[0];
        com.intersys.cache.Dataholder res = mInternal.runInstanceMethod("SubjectsGetObject", args, com.intersys.objects.Database.RET_PRIM);
        return res.getOid();
    }

    /**
     <p>Runs method SubjectsGetObject in Cache.</p>
     @param force represented as java.lang.Integer
     @throws com.intersys.objects.CacheException if any error occured while running the method.
     @see <a href = "http://cache-server:57772/csp/documatic/%25CSP.Documatic.cls?APP=1&PAGE=CLASS&LIBRARY=TPCS&CLASSNAME=Institute.Speciality#SubjectsGetObject"> Method SubjectsGetObject</A>
    */
    public com.intersys.objects.Oid SubjectsGetObject(java.lang.Integer force) throws com.intersys.objects.CacheException {
        com.intersys.cache.Dataholder[] args = new com.intersys.cache.Dataholder[1];
        args[0] = new com.intersys.cache.Dataholder(force);
        com.intersys.cache.Dataholder res = mInternal.runInstanceMethod("SubjectsGetObject", args, com.intersys.objects.Database.RET_PRIM);
        return res.getOid();
    }

    /**
     <p>Runs method SubjectsGetObjectId in Cache.</p>
     default argument force set to 0
     @throws com.intersys.objects.CacheException if any error occured while running the method.
     @see #SubjectsGetObjectId(java.lang.Integer)
     @see <a href = "http://cache-server:57772/csp/documatic/%25CSP.Documatic.cls?APP=1&PAGE=CLASS&LIBRARY=TPCS&CLASSNAME=Institute.Speciality#SubjectsGetObjectId"> Method SubjectsGetObjectId</A>
    */
    public java.lang.String SubjectsGetObjectId() throws com.intersys.objects.CacheException {
        com.intersys.cache.Dataholder[] args = new com.intersys.cache.Dataholder[0];
        com.intersys.cache.Dataholder res = mInternal.runInstanceMethod("SubjectsGetObjectId", args, com.intersys.objects.Database.RET_PRIM);
        return res.getString();
    }

    /**
     <p>Runs method SubjectsGetObjectId in Cache.</p>
     @param force represented as java.lang.Integer
     @throws com.intersys.objects.CacheException if any error occured while running the method.
     @see <a href = "http://cache-server:57772/csp/documatic/%25CSP.Documatic.cls?APP=1&PAGE=CLASS&LIBRARY=TPCS&CLASSNAME=Institute.Speciality#SubjectsGetObjectId"> Method SubjectsGetObjectId</A>
    */
    public java.lang.String SubjectsGetObjectId(java.lang.Integer force) throws com.intersys.objects.CacheException {
        com.intersys.cache.Dataholder[] args = new com.intersys.cache.Dataholder[1];
        args[0] = new com.intersys.cache.Dataholder(force);
        com.intersys.cache.Dataholder res = mInternal.runInstanceMethod("SubjectsGetObjectId", args, com.intersys.objects.Database.RET_PRIM);
        return res.getString();
    }

    /**
     <p>Runs method SubjectsIsValid in Cache.</p>
     @param db represented as com.intersys.objects.Database
     @param value represented as java.lang.String
     @throws com.intersys.objects.CacheException if any error occured while running the method.
     @see <a href = "http://cache-server:57772/csp/documatic/%25CSP.Documatic.cls?APP=1&PAGE=CLASS&LIBRARY=TPCS&CLASSNAME=Institute.Speciality#SubjectsIsValid"> Method SubjectsIsValid</A>
    */
    public static void SubjectsIsValid(com.intersys.objects.Database db, java.lang.String value) throws com.intersys.objects.CacheException {
        com.intersys.cache.Dataholder[] args = new com.intersys.cache.Dataholder[1];
        args[0] = new com.intersys.cache.Dataholder(value);
        com.intersys.cache.Dataholder res = db.runClassMethod(CACHE_CLASS_NAME, "SubjectsIsValid", args, com.intersys.objects.Database.RET_PRIM);
        db.parseStatus(res);
        return;
    }

    /**
     <p>Returns a CallableStatement for query Extent.</p>
     @param db represented as com.intersys.objects.Database
     @throws com.intersys.objects.CacheException if any error occured while running the method.
    */
    public static com.intersys.objects.CacheQuery query_Extent(com.intersys.objects.Database db) throws com.intersys.objects.CacheException {
        return new com.intersys.objects.CacheQuery(db, "Institute.Speciality_Extent", 0, 0);
    }

    public static Object addToBatchInsert(Object batch, java.sql.Connection con, String cos_Name, String cos_Number, Integer cos_Semester) throws java.sql.SQLException {
        if (batch == null) {
            com.intersys.jdbc.CacheConnection c = null;
            if (con != null) {
                try {
                    c = com.intersys.cache.jdbcutil.JDBCAdapter.getCacheConnection(con);
                } catch (com.intersys.objects.CacheException x) {
                    throw new java.sql.SQLException("Connection is not a CacheConnection.");
                }
            }
            batch = new com.intersys.jdbc.QuickStatement.Batch(c.getConnectionInfo());
        }
        com.intersys.jdbc.QuickStatement.Batch qbatch = (com.intersys.jdbc.QuickStatement.Batch) batch;
        com.intersys.jdbc.SysListProxy.setInteger(qbatch.list, 4);
        com.intersys.jdbc.SysListProxy.setString(qbatch.list, cos_Name);
        com.intersys.jdbc.SysListProxy.setString(qbatch.list, cos_Number);
        com.intersys.jdbc.SysListProxy.setIntegerWrapper(qbatch.list, cos_Semester);
        com.intersys.jdbc.SysListProxy.setUndefined(qbatch.list);
        qbatch.flushRecord();
        return qbatch;
    }

    public static java.util.List executeBatchInsert(java.sql.Connection con, Object batch, int nolock) throws java.sql.SQLException {
        Object ids = com.intersys.jdbc.QuickStatement.Batch.execute("Institute", "Speciality", 4, batch, con, nolock);
        return new com.intersys.objects.SList(ids);
    }
}
