package org.ode4j.ode.internal.gimpact;

import static org.ode4j.ode.internal.gimpact.GimGeometry.*;
import org.cpp4j.java.IntArray;
import org.cpp4j.java.ObjArray;
import org.ode4j.ode.internal.gimpact.GimBufferArrayFloat.GIM_PROCESS_BUFFER_ARRAY_FN;
import org.ode4j.ode.internal.gimpact.GimTriCollision.GIM_TRIANGLE_DATA;
import org.ode4j.ode.internal.gimpact.GimTriCollision.GIM_TRIANGLE_RAY_CONTACT_DATA;
import org.ode4j.ode.internal.gimpact.GimTriCollision.GIM_TRIPLANES_CACHE;
import org.ode4j.ode.internal.gimpact.GimTrimeshCapsuleCollision.GIM_CAPSULE_DATA;

/**
	A Trimesh is the basic geometric structure for representing solid objects.
	<p><strong>CREATING TRIMESHES</strong></p>
	<ul>
	<li> For creating trimeshes, you must initialize Buffer managers by calling \ref gimpact_init
	<li> Then you must define the vertex and index sources by creating them with \ref BUFFER_ARRAYS routines, and then call  \ref gim_trimesh_create_from_arrays.
	<li> An alternative way for creaing trimesh objects is calling  \ref gim_trimesh_create_from_data.
	<li> For access to the trimesh data (vertices, triangle indices), you must call  \ref gim_trimesh_locks_work_data , and  \ref gim_trimesh_unlocks_work_data for finish the access.
	<li> Each time when the trimesh data is modified, you must call  \ref gim_trimesh_update after.
	<li> When a trimesh is no longer needed, you must call \ref gim_trimesh_destroy.
	</ul>

	<p>This is an example of how to create a deformable trimesh that shares vertices with the user application:</p>
	<code>
	//Declaration of vertices
	vec3f trimeshvertices[200];
	//Declaration of indices
	GUINT trimeshindices[100];

	... Initializing vertices and triangle indices at beginning

	//Then create trimesh
	GIM_TRIMESH mytrimesh;

	//Calling trimesh create function

	gim_trimesh_create_from_data(
	&mytrimesh,
	trimeshvertices,200,
	0 ,//copy_vertices is 0
	trimeshindices,
	100,
	0, //copy_indices is 0
	0 //transformed_reply is 0
	);
	</code>
	<p>Note that parameter transformed_reply is 0, that means that m_transformed_vertex_buffer is a reference to m_source_vertex on the trimesh, and transformations are not avaliable. Use that configuration if you have to simulate a deformable trimesh like cloth or elastic bodies.</p>
	<p>When the trimesh is no longer needed, destroy it safely with gim_trimesh_destroy()</p>
	<p><strong>UPDATING TRIMESHES</strong></p>
	<p>On simulation loops, is needed to update trimeshes every time for update vertices althought updating triangle boxes and planes cache. There is two ways for update trimeshes: </p>
	<ul>
	<li> Updating vertices directly. You need to access to the \ref GIM_TRIMESH.m_source_vertex_buffer member; a vertex buffer which has access to the source vertices.
	<code>
	// Access to the source vertices
	gim_buffer_array_lock(&mytrimesh.m_source_vertex_buffer, G_MA_READ_WRITE);

	//Get a pointer to the vertex buffer
	vec3f * vertexpointer = GIM_BUFFER_ARRAY_POINTER(vec3f,mytrimesh.m_source_vertex_buffer,0);

	//Get the amount of vertices
	int veccount = mytrimesh.m_source_vertex_buffer.m_element_count;

	//Modify vertices
	for (int i=0;i<veccount ;i++ )
	{
	    .....
	    .....
	    processing vertices
	    .....
		.....
	}

	// Don't forget to unlock the source vertex array
	gim_buffer_array_unlock(&mytrimesh.m_source_vertex_buffer);

	// Notify that the state of the trimesh is changed
	gim_trimesh_post_update(&mytrimesh.m_source_vertex_buffer);

	</code>
	For making trimeshes that allow to update their vertices, use \ref gim_trimesh_create_from_data with parameter <strong>transformed_reply</strong> = 0.
	</ul>
	<ul>
	<li> Aplying a transformation. Simply use \ref gim_trimesh_set_tranform . Remember that with this method trimeshes must be created with \ref gim_trimesh_create_from_data with parameter <strong>transformed_reply</strong> = 1.
	</ul>
	<p> After updating vertices, you must call \ref gim_trimesh_update()</p>
	<p><strong>TRIMESHES COLLISION</strong></p>
	<p>Before collide trimeshes, you need to update them first.</p>
	<p>Then you must use \ref gim_trimesh_trimesh_collision().</p>
 * @author Francisco Leon
 */
public class GimTrimesh implements GimConstants {

    private static final int GIM_TRIMESH_TRANSFORMED_REPLY = 1;

    private static final int GIM_TRIMESH_NEED_UPDATE = 2;

    interface gim_update_trimesh_function {

        void run(GimTrimesh gt);
    }

    GimBufferArrayFloat m_source_vertex_buffer;

    /** 
	 * (GUINT) Indices of triangles,groups of three elements.
	 * Array of GUINT. Triangle indices. Each triple contains indices of the vertices for each triangle.
	 * @invariant must be aligned
	 */
    GimBufferArrayInt m_tri_index_buffer;

    int m_mask;

    /**
	 * Allocated transformed vertices vec3f.
	 * Array of vec3f.If gim_trimesh_has_tranformed_reply(this) == 1 then it refers to the m_source_vertex_buffer.
	 * @invariant must be aligned
	 */
    GimBufferArrayFloat m_transformed_vertex_buffer = new GimBufferArrayFloat();

    GimAABBSet m_aabbset;

    GimDynArray<GIM_TRIPLANES_CACHE> m_planes_cache_buffer;

    GimBitSet m_planes_cache_bitset;

    gim_update_trimesh_function m_update_callback;

    mat4f m_transform = new mat4f();

    public void gim_trimesh_trimesh_collision(GimTrimesh trimesh2, GimDynArray<GimContact> contacts) {
        GimTrimeshTrimeshCol.gim_trimesh_trimesh_collision(this, trimesh2, contacts);
    }

    public void gim_trimesh_sphere_collision(vec3f center, float radius, GimDynArray<GimContact> contacts) {
        GimTrimeshSphereCollision.gim_trimesh_sphere_collision(this, center, radius, contacts);
    }

    public void gim_trimesh_capsule_collision(GIM_CAPSULE_DATA capsule, GimDynArray<GimContact> contacts) {
        GimTrimeshCapsuleCollision.gim_trimesh_capsule_collision(this, capsule, contacts);
    }

    public static GimDynArray<vec4f> GIM_CREATE_TRIMESHPLANE_CONTACTS() {
        return GimDynArray.GIM_DYNARRAY_CREATE(GimDynArray.G_ARRAY_GROW_SIZE);
    }

    public void gim_trimesh_plane_collision(vec4f plane, GimDynArray<vec4f> contacts) {
        GimTrimeshTrimeshCol.gim_trimesh_plane_collision(this, plane, contacts);
    }

    public int gim_trimesh_ray_collision(vec3f origin, vec3f dir, final float tmax, GIM_TRIANGLE_RAY_CONTACT_DATA contact) {
        return GimTrimeshRayCollision.gim_trimesh_ray_collision(this, origin, dir, tmax, contact);
    }

    public int gim_trimesh_ray_closest_collision(vec3f origin, vec3f dir, float tmax, GIM_TRIANGLE_RAY_CONTACT_DATA contact) {
        return GimTrimeshRayCollision.gim_trimesh_ray_closest_collision(this, origin, dir, tmax, contact);
    }

    public int gim_trimesh_get_triangle_count() {
        return m_tri_index_buffer.getElementCount() / 3;
    }

    static GimTrimesh gim_trimesh_create_from_arrays(GimBufferArrayFloat vertex_array, GimBufferArrayInt triindex_array, final boolean transformed_reply) {
        GimTrimesh trimesh = new GimTrimesh();
        assert (trimesh != null);
        assert (vertex_array != null);
        assert (triindex_array != null);
        trimesh.m_source_vertex_buffer = vertex_array;
        trimesh.m_tri_index_buffer = triindex_array;
        trimesh.m_mask = GIM_TRIMESH_NEED_UPDATE;
        if (transformed_reply) {
            trimesh.m_mask |= GIM_TRIMESH_TRANSFORMED_REPLY;
            trimesh.m_transformed_vertex_buffer = vertex_array.cloneValues();
        } else {
            trimesh.m_transformed_vertex_buffer = vertex_array.cloneRefs();
        }
        int facecount = trimesh.gim_trimesh_get_triangle_count();
        trimesh.m_aabbset = GimAABBSet.gim_aabbset_alloc(facecount);
        trimesh.m_planes_cache_buffer = GimDynArray.GIM_DYNARRAY_CREATE_SIZED(facecount);
        trimesh.m_planes_cache_bitset = GimBitSet.GIM_BITSET_CREATE_SIZED(facecount);
        trimesh.m_update_callback = null;
        IDENTIFY_MATRIX_4X4(trimesh.m_transform);
        return trimesh;
    }

    public static GimTrimesh gim_trimesh_create_from_data(float[] vertex_array, boolean copy_vertices, int[] triindex_array, boolean copy_indices, boolean transformed_reply) {
        GimTrimesh THIS;
        GimBufferArrayFloat buffer_vertex_array;
        GimBufferArrayInt buffer_triindex_array;
        if (copy_vertices) {
            buffer_vertex_array = GimBufferArrayFloat.createCopy(vertex_array);
        } else {
            buffer_vertex_array = GimBufferArrayFloat.createRef(vertex_array);
        }
        if (copy_indices) {
            buffer_triindex_array = GimBufferArrayInt.createCopy(triindex_array);
        } else {
            buffer_triindex_array = GimBufferArrayInt.createRef(triindex_array);
        }
        THIS = gim_trimesh_create_from_arrays(buffer_vertex_array, buffer_triindex_array, transformed_reply);
        buffer_vertex_array.GIM_BUFFER_ARRAY_DESTROY();
        buffer_triindex_array.GIM_BUFFER_ARRAY_DESTROY();
        return THIS;
    }

    public void gim_trimesh_destroy() {
        m_aabbset.gim_aabbset_destroy();
        m_planes_cache_buffer.GIM_DYNARRAY_DESTROY();
        m_planes_cache_bitset.GIM_DYNARRAY_DESTROY();
        m_transformed_vertex_buffer.GIM_BUFFER_ARRAY_DESTROY();
        m_source_vertex_buffer.GIM_BUFFER_ARRAY_DESTROY();
        m_tri_index_buffer.GIM_BUFFER_ARRAY_DESTROY();
    }

    static GimTrimesh gim_trimesh_copy(GimTrimesh source_trimesh, boolean copy_by_reference, boolean transformed_reply) {
        if (copy_by_reference) {
            System.out.println("Copying TRIMESH by ref is not supported.");
        }
        {
            GimBufferArrayFloat buffer_vertex_array;
            GimBufferArrayInt buffer_triindex_array;
            buffer_vertex_array = source_trimesh.m_source_vertex_buffer.cloneValues();
            buffer_triindex_array = source_trimesh.m_tri_index_buffer.cloneValues();
            GimTrimesh dest_trimesh = gim_trimesh_create_from_arrays(buffer_vertex_array, buffer_triindex_array, transformed_reply);
            buffer_vertex_array.GIM_BUFFER_ARRAY_DESTROY();
            buffer_triindex_array.GIM_BUFFER_ARRAY_DESTROY();
            return dest_trimesh;
        }
    }

    public void gim_trimesh_locks_work_data() {
    }

    public void gim_trimesh_unlocks_work_data() {
    }

    boolean gim_trimesh_has_tranformed_reply() {
        return (m_mask & GIM_TRIMESH_TRANSFORMED_REPLY) != 0;
    }

    boolean gim_trimesh_needs_update() {
        return (m_mask & GIM_TRIMESH_NEED_UPDATE) != 0;
    }

    void gim_trimesh_post_update() {
        m_mask |= GIM_TRIMESH_NEED_UPDATE;
    }

    private final GIM_PROCESS_BUFFER_ARRAY_FN MULT_MAT_VEC4_KERNEL = new GIM_PROCESS_BUFFER_ARRAY_FN() {

        public void run(mat4f _mat, vec3f _src, vec3f _dst) {
            MAT_DOT_VEC_3X4(_dst, _mat, _src);
        }
    };

    void gim_trimesh_update_vertices() {
        if (gim_trimesh_has_tranformed_reply() == false) return;
        GimBufferArrayFloat psource_vertex_buffer = m_source_vertex_buffer;
        GimBufferArrayFloat ptransformed_vertex_buffer = m_transformed_vertex_buffer;
        mat4f transform = new mat4f();
        COPY_MATRIX_4X4(transform, m_transform);
        GimBufferArrayFloat.GIM_PROCESS_BUFFER_ARRAY(transform, psource_vertex_buffer, ptransformed_vertex_buffer, MULT_MAT_VEC4_KERNEL);
    }

    void gim_trimesh_update_aabbset() {
        ObjArray<vec3f> transformed_vertices = m_transformed_vertex_buffer.GIM_BUFFER_ARRAY_POINTER(0);
        assert (transformed_vertices != null);
        IntArray triangle_indices = m_tri_index_buffer.GIM_BUFFER_ARRAY_POINTER(0);
        assert (triangle_indices != null);
        int triangle_count = gim_trimesh_get_triangle_count();
        vec3f v1, v2, v3;
        int i;
        for (i = 0; i < triangle_count; i++) {
            COMPUTEAABB_FOR_TRIANGLE(m_aabbset.at(i), transformed_vertices.at(triangle_indices.at(0)), transformed_vertices.at(triangle_indices.at(1)), transformed_vertices.at(triangle_indices.at(2)));
            triangle_indices.inc(3);
        }
        m_planes_cache_bitset.GIM_BITSET_CLEAR_ALL();
        m_aabbset.gim_aabbset_update();
    }

    public void gim_trimesh_update() {
        if (gim_trimesh_needs_update() == false) return;
        gim_trimesh_update_vertices();
        gim_trimesh_locks_work_data();
        gim_trimesh_update_aabbset();
        gim_trimesh_unlocks_work_data();
        m_mask &= ~GIM_TRIMESH_NEED_UPDATE;
    }

    public void gim_trimesh_set_tranform(mat4f transform) {
        float diff = 0.0f;
        float[] originaltrans = m_transform.f;
        float[] newtrans = transform.f;
        int i;
        for (i = 0; i < 16; i++) {
            diff += Math.abs(originaltrans[i] - newtrans[i]);
        }
        if (diff < 0.00001f) return;
        COPY_MATRIX_4X4(m_transform, transform);
        gim_trimesh_post_update();
    }

    void gim_trimesh_get_triangle_data(int triangle_index, final GIM_TRIANGLE_DATA tri_data) {
        ObjArray<vec3f> transformed_vertices = m_transformed_vertex_buffer.GIM_BUFFER_ARRAY_POINTER(0);
        IntArray triangle_indices = m_tri_index_buffer.GIM_BUFFER_ARRAY_POINTER(triangle_index * 3);
        VEC_COPY(tri_data.m_vertices[0], transformed_vertices.at(triangle_indices.at(0)));
        VEC_COPY(tri_data.m_vertices[1], transformed_vertices.at(triangle_indices.at(1)));
        VEC_COPY(tri_data.m_vertices[2], transformed_vertices.at(triangle_indices.at(2)));
        ObjArray<GIM_TRIPLANES_CACHE> planes = m_planes_cache_buffer.GIM_DYNARRAY_POINTER_V();
        planes.inc(triangle_index);
        if (planes.at0() == null) planes.setAt0(new GIM_TRIPLANES_CACHE());
        GIM_TRIPLANES_CACHE plane = planes.at0();
        boolean bit_eval;
        bit_eval = m_planes_cache_bitset.GIM_BITSET_GET(triangle_index);
        if (bit_eval == false) {
            TRIANGLE_PLANE(tri_data.m_vertices[0], tri_data.m_vertices[1], tri_data.m_vertices[2], plane.m_planes[0]);
            EDGE_PLANE(tri_data.m_vertices[0], tri_data.m_vertices[1], plane.m_planes[0], plane.m_planes[1]);
            EDGE_PLANE(tri_data.m_vertices[1], tri_data.m_vertices[2], plane.m_planes[0], plane.m_planes[2]);
            EDGE_PLANE(tri_data.m_vertices[2], tri_data.m_vertices[0], plane.m_planes[0], plane.m_planes[3]);
            m_planes_cache_bitset.GIM_BITSET_SET(triangle_index);
        }
        VEC_COPY_4(tri_data.m_planes.m_planes[0], plane.m_planes[0]);
        VEC_COPY_4(tri_data.m_planes.m_planes[1], plane.m_planes[1]);
        VEC_COPY_4(tri_data.m_planes.m_planes[2], plane.m_planes[2]);
        VEC_COPY_4(tri_data.m_planes.m_planes[3], plane.m_planes[3]);
    }

    public void gim_trimesh_get_triangle_vertices(int triangle_index, vec3f v1, vec3f v2, vec3f v3) {
        ObjArray<vec3f> transformed_vertices = m_transformed_vertex_buffer.GIM_BUFFER_ARRAY_POINTER(0);
        IntArray triangle_indices = m_tri_index_buffer.GIM_BUFFER_ARRAY_POINTER(triangle_index * 3);
        VEC_COPY(v1, transformed_vertices.at(triangle_indices.at(0)));
        VEC_COPY(v2, transformed_vertices.at(triangle_indices.at(1)));
        VEC_COPY(v3, transformed_vertices.at(triangle_indices.at(2)));
    }

    public GimAABBSet getAabbSet() {
        return m_aabbset;
    }
}
