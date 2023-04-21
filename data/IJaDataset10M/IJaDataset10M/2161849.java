package com.frinika.ejb;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

public class SongEntity implements java.io.Serializable, IdObject {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    Collection<MidiLaneEntity> midiLanes;

    ComposerEntity composer;

    String title;

    SongEntity parent;

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Collection<MidiLaneEntity> getMidiLanes() {
        return midiLanes;
    }

    public void setMidiLanes(Collection<MidiLaneEntity> midiLanes) {
        this.midiLanes = midiLanes;
    }

    public ComposerEntity getComposer() {
        return composer;
    }

    public void setComposer(ComposerEntity composer) {
        this.composer = composer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public SongEntity getParent() {
        return parent;
    }

    public void setParent(SongEntity parent) {
        this.parent = parent;
    }
}
