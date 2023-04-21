package org.scaroo.injectedgallery;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author alexandremazari
 */
@Entity
public class Picture {

    @Id
    private String name;
}
