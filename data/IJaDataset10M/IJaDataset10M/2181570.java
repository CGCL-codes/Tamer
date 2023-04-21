package br.edu.utfpr.mathgame.beans;

import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

/**
 *
 * @author jteodoro
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Choose extends Scene {

    @ManyToMany(targetEntity = Item.class, cascade = { CascadeType.REFRESH })
    @JoinTable(name = "choose_scenes", joinColumns = @JoinColumn(name = "choose_id"), inverseJoinColumns = @JoinColumn(name = "scene_id"))
    private Set<Option> options;

    public Set<Option> getOptions() {
        return options;
    }

    public void setOptions(Set<Option> options) {
        this.options = options;
    }
}
