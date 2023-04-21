package models;

import siena.Generator;
import siena.Id;
import siena.Table;

/**
 * Image file ressource (gif, jpg, png, etc).
 * 
 * @author slever
 *
 */
@Table("images")
public class Image extends Resource {

    @Id(Generator.AUTO_INCREMENT)
    public Long id;

    public String path;
}
