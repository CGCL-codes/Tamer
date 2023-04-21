package de.grogra.imp3d.gl20;

public class GL20ResourceShaderFragmentBlend extends GL20ResourceShaderFragment {

    public GL20ResourceShaderFragmentBlend() {
        super(GL20Resource.GL20RESOURCE_SHADERFRAGMENT_BLEND);
    }

    @Override
    public boolean fragmentAffectOnAlpha() {
        return false;
    }
}
