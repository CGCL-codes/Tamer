package edu.csula.coolstatela.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity(name = "instantiation_asset")
public class InstantiationAsset {

    private Long id;

    private StoryInstantiation storyInstantiation;

    private Media asset;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "instantiationID")
    public StoryInstantiation getStoryInstantiation() {
        return storyInstantiation;
    }

    public void setStoryInstantiation(StoryInstantiation storyInstantiation) {
        this.storyInstantiation = storyInstantiation;
    }

    @ManyToOne
    @JoinColumn(name = "assetID")
    public Media getAsset() {
        return asset;
    }

    public void setAsset(Media asset) {
        this.asset = asset;
    }
}
