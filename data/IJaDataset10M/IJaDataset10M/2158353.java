package org.encog.plugins.opencl.example;

import java.nio.FloatBuffer;
import org.encog.util.file.ResourceInputStream;
import com.nativelibs4java.opencl.CLBuildException;
import com.nativelibs4java.opencl.CLContext;
import com.nativelibs4java.opencl.CLEvent;
import com.nativelibs4java.opencl.CLFloatBuffer;
import com.nativelibs4java.opencl.CLKernel;
import com.nativelibs4java.opencl.CLMem.Usage;
import com.nativelibs4java.opencl.CLProgram;
import com.nativelibs4java.opencl.CLQueue;
import com.nativelibs4java.opencl.JavaCL;
import com.nativelibs4java.util.NIOUtils;

public class SimpleTest {

    public static void main(String[] args) {
        System.out.println("Start");
        CLContext context = JavaCL.createBestContext();
        CLQueue queue = context.createDefaultQueue();
        float[] A = { 1.0f, 2.0f, 3.0f, 4.0f };
        float[] B = { 5.0f, 6.0f, 7.0f, 8.0f };
        String myKernelSource = ResourceInputStream.readResourceAsString("org/encog/plugins/opencl/kernels/simple.cl");
        FloatBuffer array1 = NIOUtils.directFloats(10, context.getByteOrder());
        FloatBuffer array2 = NIOUtils.directFloats(10, context.getByteOrder());
        FloatBuffer resultArray = NIOUtils.directFloats(10, context.getByteOrder());
        array1.put(A);
        array2.put(B);
        CLFloatBuffer b1 = context.createFloatBuffer(Usage.Input, array1, true);
        CLFloatBuffer b2 = context.createFloatBuffer(Usage.Input, array2, true);
        CLFloatBuffer b3 = context.createFloatBuffer(Usage.Output, resultArray, false);
        CLProgram program;
        try {
            program = context.createProgram(myKernelSource).build();
            CLKernel kernel = program.createKernel("simpleKernel", b1, b2, b3);
            CLEvent kernelCompletion;
            synchronized (kernel) {
                kernelCompletion = kernel.enqueueNDRange(queue, new int[] { A.length }, new int[] { 1 });
            }
            kernelCompletion.waitFor();
            FloatBuffer f = b3.read(queue, kernelCompletion);
            for (int i = 0; i < A.length; i++) {
                System.out.println(A[i] + " * " + B[i] + " = " + f.get(i));
            }
        } catch (CLBuildException e) {
            e.printStackTrace();
        }
    }
}
