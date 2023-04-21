package jp.co.lattice.vProcessor.base;

import java.io.*;
import jp.co.lattice.vProcessor.com.*;
import jp.co.lattice.vProcessor.node.*;
import jp.co.lattice.vProcessor.parse.*;
import jp.co.lattice.vKernel.core.c0.*;

/**
 * @author	  created by Eishin Matsui (00/06/29-)
 */
public class x3pParseEx extends x3pParse {

    /**
	 *	�R���X�g���N�^
	 */
    public x3pParseEx(x3pGlobal dt, InputStream in) throws lvThrowable {
        super(dt, in);
    }

    protected x3pXvlShape NewXvlShape() {
        return new x3pXvlShapeEx();
    }

    protected x3pContent NewXvlFaces() {
        return new x3pXvlFacesEx();
    }

    protected x3pMaterial NewMaterial() {
        return new x3pMaterialEx();
    }

    protected x3pImageTexture NewImageTexture() {
        return new x3pImageTextureEx();
    }
}
