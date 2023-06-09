package org.ogre4j;

import org.xbig.base.*;

public interface ITechnique extends INativeObject, org.ogre4j.IResourceAllocatedObject {

    public static interface IGPUDeviceNameRule extends INativeObject {

        /** **/
        public String getdevicePattern();

        /** **/
        public void setdevicePattern(String _jni_value_);

        /** **/
        public org.ogre4j.Technique.IncludeOrExclude getincludeOrExclude();

        /** **/
        public void setincludeOrExclude(org.ogre4j.Technique.IncludeOrExclude _jni_value_);

        /** **/
        public boolean getcaseSensitive();

        /** **/
        public void setcaseSensitive(boolean _jni_value_);
    }

    public static interface IGPUVendorRule extends INativeObject {

        /** **/
        public org.ogre4j.GPUVendor getvendor();

        /** **/
        public void setvendor(org.ogre4j.GPUVendor _jni_value_);

        /** **/
        public org.ogre4j.Technique.IncludeOrExclude getincludeOrExclude();

        /** **/
        public void setincludeOrExclude(org.ogre4j.Technique.IncludeOrExclude _jni_value_);
    }

    public interface IPasses extends INativeObject, org.std.Ivector<org.ogre4j.IPass> {

        /** **/
        public void assign(int num, org.ogre4j.IPass val);

        /** **/
        public org.ogre4j.IPass at(int loc);

        /** **/
        public org.ogre4j.IPass back();

        /** **/
        public int capacity();

        /** **/
        public void clear();

        /** **/
        public boolean empty();

        /** **/
        public org.ogre4j.IPass front();

        /** **/
        public int max_size();

        /** **/
        public void pop_back();

        /** **/
        public void push_back(org.ogre4j.IPass val);

        /** **/
        public void reserve(int size);

        /** **/
        public int size();
    }

    public interface IGPUVendorRuleList extends INativeObject, org.std.Ivector<org.ogre4j.ITechnique.IGPUVendorRule> {

        /** **/
        public void assign(int num, org.ogre4j.ITechnique.IGPUVendorRule val);

        /** **/
        public org.ogre4j.ITechnique.IGPUVendorRule at(int loc);

        /** **/
        public org.ogre4j.ITechnique.IGPUVendorRule back();

        /** **/
        public int capacity();

        /** **/
        public void clear();

        /** **/
        public boolean empty();

        /** **/
        public org.ogre4j.ITechnique.IGPUVendorRule front();

        /** **/
        public int max_size();

        /** **/
        public void pop_back();

        /** **/
        public void push_back(org.ogre4j.ITechnique.IGPUVendorRule val);

        /** **/
        public void reserve(int size);

        /** **/
        public int size();
    }

    public interface IGPUDeviceNameRuleList extends INativeObject, org.std.Ivector<org.ogre4j.ITechnique.IGPUDeviceNameRule> {

        /** **/
        public void assign(int num, org.ogre4j.ITechnique.IGPUDeviceNameRule val);

        /** **/
        public org.ogre4j.ITechnique.IGPUDeviceNameRule at(int loc);

        /** **/
        public org.ogre4j.ITechnique.IGPUDeviceNameRule back();

        /** **/
        public int capacity();

        /** **/
        public void clear();

        /** **/
        public boolean empty();

        /** **/
        public org.ogre4j.ITechnique.IGPUDeviceNameRule front();

        /** **/
        public int max_size();

        /** **/
        public void pop_back();

        /** **/
        public void push_back(org.ogre4j.ITechnique.IGPUDeviceNameRule val);

        /** **/
        public void reserve(int size);

        /** **/
        public int size();
    }

    public interface IPassIterator extends INativeObject, org.ogre4j.IVectorIterator<org.ogre4j.ITechnique.IPasses> {

        /** **/
        public boolean hasMoreElements();

        /** **/
        public org.ogre4j.IPass getNext();

        /** **/
        public org.ogre4j.IPass peekNext();

        /** **/
        public NativeObjectPointer<org.ogre4j.IPass> peekNextPtr();

        /** **/
        public void moveNext();
    }

    public interface IIlluminationPassIterator extends INativeObject, org.ogre4j.IVectorIterator<org.ogre4j.IIlluminationPassList> {

        /** **/
        public boolean hasMoreElements();

        /** **/
        public org.ogre4j.IIlluminationPass getNext();

        /** **/
        public org.ogre4j.IIlluminationPass peekNext();

        /** **/
        public NativeObjectPointer<org.ogre4j.IIlluminationPass> peekNextPtr();

        /** **/
        public void moveNext();
    }

    public interface IGPUVendorRuleIterator extends INativeObject, org.ogre4j.IConstVectorIterator<org.ogre4j.ITechnique.IGPUVendorRuleList> {

        /** **/
        public boolean hasMoreElements();

        /** **/
        public void getNext(org.ogre4j.ITechnique.IGPUVendorRule returnValue);

        /** **/
        public void peekNext(org.ogre4j.ITechnique.IGPUVendorRule returnValue);

        /** **/
        public org.ogre4j.ITechnique.IGPUVendorRule peekNextPtr();

        /** **/
        public void moveNext();
    }

    public interface IGPUDeviceNameRuleIterator extends INativeObject, org.ogre4j.IConstVectorIterator<org.ogre4j.ITechnique.IGPUDeviceNameRuleList> {

        /** **/
        public boolean hasMoreElements();

        /** **/
        public void getNext(org.ogre4j.ITechnique.IGPUDeviceNameRule returnValue);

        /** **/
        public void peekNext(org.ogre4j.ITechnique.IGPUDeviceNameRule returnValue);

        /** **/
        public org.ogre4j.ITechnique.IGPUDeviceNameRule peekNextPtr();

        /** **/
        public void moveNext();
    }

    /** 
    Indicates if this technique is supported by the current graphics card. **/
    public boolean isSupported();

    /** 
    Internal compilation method; see . **/
    public String _compile(boolean autoManageTextureUnits);

    /** **/
    public boolean checkGPURules(org.std.Iostringstream errors);

    /** **/
    public boolean checkHardwareSupport(boolean autoManageTextureUnits, org.std.Iostringstream compileErrors);

    /** 
    Internal method for splitting the passes into illumination passes. **/
    public void _compileIlluminationPasses();

    /** 
    Creates a new  for this . **/
    public org.ogre4j.IPass createPass();

    /** 
    Retrieves the  with the given index. **/
    public org.ogre4j.IPass getPass(int index);

    /** 
    Retrieves the  matching name. Returns 0 if name match is not found. **/
    public org.ogre4j.IPass getPass(String name);

    /** 
    Retrieves the number of passes. **/
    public int getNumPasses();

    /** 
    Removes the  with the given index. **/
    public void removePass(int index);

    /** 
    Removes all Passes from this . **/
    public void removeAllPasses();

    /** 
    Move a pass from source index to destination index. If successful then returns true. **/
    public boolean movePass(int sourceIndex, int destinationIndex);

    /** 
    Gets an iterator over the passes in this . **/
    public void getPassIterator(org.ogre4j.ITechnique.IPassIterator returnValue);

    /** 
    Gets an iterator over the illumination-stage categorised passes. **/
    public void getIlluminationPassIterator(org.ogre4j.ITechnique.IIlluminationPassIterator returnValue);

    /** **/
    public org.ogre4j.IMaterial getParent();

    /** 
    Overloaded operator to copy on  to another. **/
    public org.ogre4j.ITechnique operatorAssignment(org.ogre4j.ITechnique rhs);

    /** **/
    public String getResourceGroup();

    /** 
    Returns true if this  involves transparency. **/
    public boolean isTransparent();

    /** 
    Returns true if this  has transparent sorting enabled. **/
    public boolean isTransparentSortingEnabled();

    /** 
    Internal prepare method, derived from call to . **/
    public void _prepare();

    /** 
    Internal unprepare method, derived from call to Material::unprepare. **/
    public void _unprepare();

    /** 
    Internal load method, derived from call to . **/
    public void _load();

    /** 
    Internal unload method, derived from call to . **/
    public void _unload();

    /** **/
    public boolean isLoaded();

    /** 
    Tells the technique that it needs recompilation. **/
    public void _notifyNeedsRecompile();

    /** 
    return this material specific shadow casting specific material **/
    public void getShadowCasterMaterial(org.ogre4j.IMaterialPtr returnValue);

    /** 
    set this material specific shadow casting specific material **/
    public void setShadowCasterMaterial(org.ogre4j.IMaterialPtr val);

    /** 
    set this material specific shadow casting specific material **/
    public void setShadowCasterMaterial(String name);

    /** 
    return this material specific shadow receiving specific material **/
    public void getShadowReceiverMaterial(org.ogre4j.IMaterialPtr returnValue);

    /** 
    set this material specific shadow receiving specific material **/
    public void setShadowReceiverMaterial(org.ogre4j.IMaterialPtr val);

    /** 
    set this material specific shadow receiving specific material **/
    public void setShadowReceiverMaterial(String name);

    /** 
    Sets the point size properties for every  in this . **/
    public void setPointSize(float ps);

    /** 
    Sets the ambient colour reflectance properties for every  in every . **/
    public void setAmbient(float red, float green, float blue);

    /** 
    Sets the ambient colour reflectance properties for every  in every . **/
    public void setAmbient(org.ogre4j.IColourValue ambient);

    /** 
    Sets the diffuse colour reflectance properties of every  in every . **/
    public void setDiffuse(float red, float green, float blue, float alpha);

    /** 
    Sets the diffuse colour reflectance properties of every  in every . **/
    public void setDiffuse(org.ogre4j.IColourValue diffuse);

    /** 
    Sets the specular colour reflectance properties of every  in every . **/
    public void setSpecular(float red, float green, float blue, float alpha);

    /** 
    Sets the specular colour reflectance properties of every  in every . **/
    public void setSpecular(org.ogre4j.IColourValue specular);

    /** 
    Sets the shininess properties of every  in every . **/
    public void setShininess(float val);

    /** 
    Sets the amount of self-illumination of every  in every . **/
    public void setSelfIllumination(float red, float green, float blue);

    /** 
    Sets the amount of self-illumination of every  in every . **/
    public void setSelfIllumination(org.ogre4j.IColourValue selfIllum);

    /** 
    Sets whether or not each  renders with depth-buffer checking on or not. **/
    public void setDepthCheckEnabled(boolean enabled);

    /** 
    Sets whether or not each  renders with depth-buffer writing on or not. **/
    public void setDepthWriteEnabled(boolean enabled);

    /** 
    Sets the function used to compare depth values when depth checking is on. **/
    public void setDepthFunction(org.ogre4j.CompareFunction func);

    /** 
    Sets whether or not colour buffer writing is enabled for each . **/
    public void setColourWriteEnabled(boolean enabled);

    /** 
    Sets the culling mode for each pass based on the 'vertex winding'. **/
    public void setCullingMode(org.ogre4j.CullingMode mode);

    /** 
    Sets the manual culling mode, performed by CPU rather than hardware. **/
    public void setManualCullingMode(org.ogre4j.ManualCullingMode mode);

    /** 
    Sets whether or not dynamic lighting is enabled for every . **/
    public void setLightingEnabled(boolean enabled);

    /** 
    Sets the type of light shading required **/
    public void setShadingMode(org.ogre4j.ShadeOptions mode);

    /** 
    Sets the fogging mode applied to each pass. **/
    public void setFog(boolean overrideScene, org.ogre4j.FogMode mode, org.ogre4j.IColourValue colour, float expDensity, float linearStart, float linearEnd);

    /** 
    Sets the depth bias to be used for each . **/
    public void setDepthBias(float constantBias, float slopeScaleBias);

    /** 
    Set texture filtering for every texture unit in every **/
    public void setTextureFiltering(org.ogre4j.TextureFilterOptions filterType);

    /** 
    Sets the anisotropy level to be used for all textures. **/
    public void setTextureAnisotropy(long maxAniso);

    /** 
    Sets the kind of blending every pass has with the existing contents of the scene. **/
    public void setSceneBlending(org.ogre4j.SceneBlendType sbt);

    /** 
    Sets the kind of blending every pass has with the existing contents of the scene, using individual factors both color and alpha channels **/
    public void setSeparateSceneBlending(org.ogre4j.SceneBlendType sbt, org.ogre4j.SceneBlendType sbta);

    /** 
    Allows very fine control of blending every  with the existing contents of the scene. **/
    public void setSceneBlending(org.ogre4j.SceneBlendFactor sourceFactor, org.ogre4j.SceneBlendFactor destFactor);

    /** 
    Allows very fine control of blending every  with the existing contents of the scene, using individual factors both color and alpha channels **/
    public void setSeparateSceneBlending(org.ogre4j.SceneBlendFactor sourceFactor, org.ogre4j.SceneBlendFactor destFactor, org.ogre4j.SceneBlendFactor sourceFactorAlpha, org.ogre4j.SceneBlendFactor destFactorAlpha);

    /** 
    Assigns a level-of-detail (LOD) index to this . **/
    public void setLodIndex(int index);

    /** 
    Gets the level-of-detail index assigned to this . **/
    public int getLodIndex();

    /** 
    Set the 'scheme name' for this technique. **/
    public void setSchemeName(String schemeName);

    /** 
    Returns the scheme to which this technique is assigned. **/
    public String getSchemeName();

    /** **/
    public int _getSchemeIndex();

    /** 
    Is depth writing going to occur on this technique? **/
    public boolean isDepthWriteEnabled();

    /** 
    Is depth checking going to occur on this technique? **/
    public boolean isDepthCheckEnabled();

    /** 
    Exists colour writing disabled pass on this technique? **/
    public boolean hasColourWriteDisabled();

    /** 
    Set the name of the technique. **/
    public void setName(String name);

    /** **/
    public String getName();

    /** 
    Applies texture names to  Unit State with matching texture name aliases. All passes, and  Unit States within the technique are checked. If matching texture aliases are found then true is returned.
    **/
    public boolean applyTextureAliases(org.ogre4j.IAliasTextureNamePairList aliasList, boolean apply);

    /** 
    Add a rule which manually influences the support for this technique based on a GPU vendor. **/
    public void addGPUVendorRule(org.ogre4j.GPUVendor vendor, org.ogre4j.Technique.IncludeOrExclude includeOrExclude);

    /** 
    Add a rule which manually influences the support for this technique based on a GPU vendor. **/
    public void addGPUVendorRule(org.ogre4j.ITechnique.IGPUVendorRule rule);

    /** 
    Removes a matching vendor rule. **/
    public void removeGPUVendorRule(org.ogre4j.GPUVendor vendor);

    /** **/
    public void getGPUVendorRuleIterator(org.ogre4j.ITechnique.IGPUVendorRuleIterator returnValue);

    /** 
    Add a rule which manually influences the support for this technique based on a pattern that matches a GPU device name (e.g. '*8800*'). **/
    public void addGPUDeviceNameRule(String devicePattern, org.ogre4j.Technique.IncludeOrExclude includeOrExclude, boolean caseSensitive);

    /** 
    Add a rule which manually influences the support for this technique based on a pattern that matches a GPU device name (e.g. '*8800*'). **/
    public void addGPUDeviceNameRule(org.ogre4j.ITechnique.IGPUDeviceNameRule rule);

    /** 
    Removes a matching device name rule. **/
    public void removeGPUDeviceNameRule(String devicePattern);

    /** **/
    public void getGPUDeviceNameRuleIterator(org.ogre4j.ITechnique.IGPUDeviceNameRuleIterator returnValue);
}
