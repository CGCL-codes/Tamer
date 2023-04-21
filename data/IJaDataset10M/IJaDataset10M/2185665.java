package simple.template.layout;

/**
 * The <code>Definition</code> object is used to represent the
 * tile definition within the layout configuration file. This 
 * uses three fields to describe a source that is used by an
 * included <code>Tile</code> object. Firstly the name, this 
 * represents the handle to the tile object within the template.
 * Secondly, the value, this is used to represent the value 
 * given to the source when it is created. And, finally, the 
 * type, which provides the fully qualified class name of the
 * source implementation that is to be created for the tile.
 * 
 * @author Niall Gallagher
 *
 * @see simple.template.layout.Source
 */
interface Definition {

    /**
    * This provides the name of this source object and also the
    * name of the <code>Tile</code> that uses it within the 
    * template. The name is used as a handle by the template 
    * to reference the included tile. If the source needs to be
    * displayed this name is used by the template to reference
    * the tile object, which will then use this source object.
    *
    * @return this returns the name for this source object
    */
    public String getName();

    /**
    * This provides the value of the source object that is to be
    * created. The value is used by the source so that it can be
    * configured in such a way to provide the correct value to
    * the <code>Tile</code> once referenced within the template.
    *
    * @return this returns the value used by the source object
    */
    public String getValue();

    /**
    * This provides the fully qualified class name of the source
    * object that is to be created. This is used to instantiate
    * the <code>Frame</code> implementation so that it can be 
    * used by a <code>Tile</code> referenced within the template.
    *
    * @return this returns the class name of the source object
    */
    public String getType();
}
