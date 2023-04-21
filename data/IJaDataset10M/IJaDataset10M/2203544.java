package org.xith3d.render.lwjgl;

/**
 * Insert package comments here
 * 
 * Originally Coded by David Yazel on Sep 20, 2003 at 1:51:18 PM.
 * 
 * @author Marvin Froehlich (aka Qudus) [code cleaning]
 */
public class OpenGlExtensions {

    public static boolean ARB_vertex_buffer_object = false;

    public static boolean GL_ARB_vertex_program = false;

    public static boolean GL_ARB_fragment_program = false;

    public static boolean GL_ARB_texture_cube_map = false;

    public static boolean GL_EXT_texture_filter_anisotropic = false;

    public static boolean GL_EXT_separate_specular_color = false;

    public static boolean GL_EXT_texture_cube_map = false;

    public static boolean GL_ARB_transpose_matrix = false;

    public static boolean GL_NV_texgen_reflection = false;

    public static boolean GL_KTX_buffer_region = false;

    /**
     * sets the static booleans for the extensions used by the renderer.
     * 
     * @param extensions String with all the extentions in it
     */
    public static void setExtensions(String extensions) {
        ARB_vertex_buffer_object = (extensions.indexOf("ARB_vertex_buffer_object") >= 0);
        GL_ARB_vertex_program = (extensions.indexOf("GL_ARB_vertex_program") >= 0);
        GL_ARB_fragment_program = (extensions.indexOf("GL_ARB_fragment_program") >= 0);
        GL_ARB_texture_cube_map = (extensions.indexOf("GL_ARB_texture_cube_map") >= 0);
        GL_EXT_texture_filter_anisotropic = (extensions.indexOf("GL_EXT_texture_filter_anisotropic") >= 0);
        GL_EXT_separate_specular_color = (extensions.indexOf("GL_EXT_separate_specular_color") >= 0);
        GL_EXT_texture_cube_map = (extensions.indexOf("GL_EXT_texture_cube_map") >= 0);
        GL_NV_texgen_reflection = (extensions.indexOf("GL_NV_texgen_reflection") >= 0);
        GL_ARB_texture_cube_map = GL_EXT_texture_cube_map = GL_NV_texgen_reflection = GL_EXT_texture_cube_map | GL_ARB_texture_cube_map | GL_NV_texgen_reflection;
        GL_ARB_transpose_matrix = (extensions.indexOf("GL_ARB_transpose_matrix") >= 0);
        GL_KTX_buffer_region = (extensions.indexOf("GL_KTX_buffer_region") >= 0);
    }
}
