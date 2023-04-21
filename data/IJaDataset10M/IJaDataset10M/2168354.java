package org.tritonus.lowlevel.tightgsm;

import java.io.*;
import java.lang.*;
import com.sts.webmeet.content.common.audio.FrameProcessor;

public class Encoder implements FrameProcessor {

    short[] e = new short[50];

    short[] so = new short[160];

    private Gsm_State g_s = new Gsm_State();

    private Long_term lg_term_Obj = new Long_term();

    private Lpc lpc_Obj = new Lpc();

    private Rpe rpe_Obj = new Rpe();

    private Short_term sh_term_Obj = new Short_term();

    private short LARc[] = new short[8];

    private short Nc[] = new short[4];

    private short Mc[] = new short[4];

    private short bc[] = new short[4];

    private short xmaxc[] = new short[4];

    private short xmc[] = new short[13 * 4];

    private int[] input_signal = new int[160];

    private byte[] frame = new byte[Gsm_Def.FRAME_SIZE];

    public int getOutputFrameSize() {
        return 33;
    }

    public int getInputFrameSize() {
        return 320;
    }

    /**	Encodes a block of data.
	 *
	 *	@param asBuffer	an 160-element array with the data to encode
	 *			int PCM 16 bit format.
	 *
	 *	@param abFrame	the encoded GSM frame (33 bytes).
	 */
    public void encode(short[] asBuffer, byte[] abFrame) {
        for (int i = 0; i < 160; i++) {
            input_signal[i] = asBuffer[i];
        }
        gsm_encode();
        System.arraycopy(frame, 0, abFrame, 0, frame.length);
    }

    public void processFrame(byte[] abPCMBuffer, int iPCMOffset, byte[] abCodedOutput, int iCodedOffset) {
        for (int i = 0, j = 0; i < 160; i++, j += 2) {
            input_signal[i] = abPCMBuffer[j + iPCMOffset] + (abPCMBuffer[j + 1 + iPCMOffset] << 8);
        }
        gsm_encode();
        System.arraycopy(frame, 0, abCodedOutput, iCodedOffset, frame.length);
    }

    private void gsm_encode() {
        int index = 0;
        Gsm_Coder_java();
        frame[index++] = (byte) (((0xD) << 4) | ((LARc[0] >> 2) & 0xF));
        frame[index++] = (byte) (((LARc[0] & 0x3) << 6) | (LARc[1] & 0x3F));
        frame[index++] = (byte) (((LARc[2] & 0x1F) << 3) | ((LARc[3] >> 2) & 0x7));
        frame[index++] = (byte) (((LARc[3] & 0x3) << 6) | ((LARc[4] & 0xF) << 2) | ((LARc[5] >> 2) & 0x3));
        frame[index++] = (byte) (((LARc[5] & 0x3) << 6) | ((LARc[6] & 0x7) << 3) | (LARc[7] & 0x7));
        frame[index++] = (byte) (((Nc[0] & 0x7F) << 1) | ((bc[0] >> 1) & 0x1));
        frame[index++] = (byte) (((bc[0] & 0x1) << 7) | ((Mc[0] & 0x3) << 5) | ((xmaxc[0] >> 1) & 0x1F));
        frame[index++] = (byte) (((xmaxc[0] & 0x1) << 7) | ((xmc[0] & 0x7) << 4) | ((xmc[1] & 0x7) << 1) | ((xmc[2] >> 2) & 0x1));
        frame[index++] = (byte) (((xmc[2] & 0x3) << 6) | ((xmc[3] & 0x7) << 3) | (xmc[4] & 0x7));
        frame[index++] = (byte) (((xmc[5] & 0x7) << 5) | ((xmc[6] & 0x7) << 2) | ((xmc[7] >> 1) & 0x3));
        frame[index++] = (byte) (((xmc[7] & 0x1) << 7) | ((xmc[8] & 0x7) << 4) | ((xmc[9] & 0x7) << 1) | ((xmc[10] >> 2) & 0x1));
        frame[index++] = (byte) ((((xmc[10] & 0x3) << 6) | ((xmc[11] & 0x7) << 3) | (xmc[12] & 0x7)));
        frame[index++] = (byte) (((Nc[1] & 0x7F) << 1) | ((bc[1] >> 1) & 0x1));
        frame[index++] = (byte) (((bc[1] & 0x1) << 7) | ((Mc[1] & 0x3) << 5) | ((xmaxc[1] >> 1) & 0x1F));
        frame[index++] = (byte) (((xmaxc[1] & 0x1) << 7) | ((xmc[13] & 0x7) << 4) | ((xmc[14] & 0x7) << 1) | ((xmc[15] >> 2) & 0x1));
        frame[index++] = (byte) (((xmc[15] & 0x3) << 6) | ((xmc[16] & 0x7) << 3) | (xmc[17] & 0x7));
        frame[index++] = (byte) (((xmc[18] & 0x7) << 5) | ((xmc[19] & 0x7) << 2) | ((xmc[20] >> 1) & 0x3));
        frame[index++] = (byte) (((xmc[20] & 0x1) << 7) | ((xmc[21] & 0x7) << 4) | ((xmc[22] & 0x7) << 1) | ((xmc[23] >> 2) & 0x1));
        frame[index++] = (byte) (((xmc[23] & 0x3) << 6) | ((xmc[24] & 0x7) << 3) | (xmc[25] & 0x7));
        frame[index++] = (byte) (((Nc[2] & 0x7F) << 1) | ((bc[2] >> 1) & 0x1));
        frame[index++] = (byte) (((bc[2] & 0x1) << 7) | ((Mc[2] & 0x3) << 5) | ((xmaxc[2] >> 1) & 0x1F));
        frame[index++] = (byte) (((xmaxc[2] & 0x1) << 7) | ((xmc[26] & 0x7) << 4) | ((xmc[27] & 0x7) << 1) | ((xmc[28] >> 2) & 0x1));
        frame[index++] = (byte) (((xmc[28] & 0x3) << 6) | ((xmc[29] & 0x7) << 3) | (xmc[30] & 0x7));
        frame[index++] = (byte) (((xmc[31] & 0x7) << 5) | ((xmc[32] & 0x7) << 2) | ((xmc[33] >> 1) & 0x3));
        frame[index++] = (byte) (((xmc[33] & 0x1) << 7) | ((xmc[34] & 0x7) << 4) | ((xmc[35] & 0x7) << 1) | ((xmc[36] >> 2) & 0x1));
        frame[index++] = (byte) (((xmc[36] & 0x3) << 6) | ((xmc[37] & 0x7) << 3) | (xmc[38] & 0x7));
        frame[index++] = (byte) (((Nc[3] & 0x7F) << 1) | ((bc[3] >> 1) & 0x1));
        frame[index++] = (byte) (((bc[3] & 0x1) << 7) | ((Mc[3] & 0x3) << 5) | ((xmaxc[3] >> 1) & 0x1F));
        frame[index++] = (byte) (((xmaxc[3] & 0x1) << 7) | ((xmc[39] & 0x7) << 4) | ((xmc[40] & 0x7) << 1) | ((xmc[41] >> 2) & 0x1));
        frame[index++] = (byte) (((xmc[41] & 0x3) << 6) | ((xmc[42] & 0x7) << 3) | (xmc[43] & 0x7));
        frame[index++] = (byte) (((xmc[44] & 0x7) << 5) | ((xmc[45] & 0x7) << 2) | ((xmc[46] >> 1) & 0x3));
        frame[index++] = (byte) (((xmc[46] & 0x1) << 7) | ((xmc[47] & 0x7) << 4) | ((xmc[48] & 0x7) << 1) | ((xmc[49] >> 2) & 0x1));
        frame[index++] = (byte) (((xmc[49] & 0x3) << 6) | ((xmc[50] & 0x7) << 3) | (xmc[51] & 0x7));
    }

    private void Gsm_Coder_java() {
        int xmc_point = 0;
        int Nc_bc_index = 0;
        int xmaxc_Mc_index = 0;
        int dp_dpp_point_dp0 = 120;
        Gsm_Preprocess();
        lpc_Obj.Gsm_LPC_Analysis(so, LARc);
        sh_term_Obj.Gsm_Short_Term_Analysis_Filter(g_s, LARc, so);
        short[] dp = g_s.getDp0();
        short[] dpp = dp;
        for (int k = 0; k <= 3; k++, xmc_point += 13) {
            lg_term_Obj.Gsm_Long_Term_Predictor(so, k * 40, e, dp, dpp, dp_dpp_point_dp0, Nc, bc, Nc_bc_index++);
            rpe_Obj.Gsm_RPE_Encoding(e, xmaxc, Mc, xmaxc_Mc_index++, xmc, xmc_point);
            for (int i = 0; i <= 39; i++) {
                dp[i + dp_dpp_point_dp0] = Add.GSM_ADD(e[5 + i], dpp[i + dp_dpp_point_dp0]);
            }
            g_s.setDp0(dp);
            dp_dpp_point_dp0 += 40;
        }
        for (int i = 0; i < 120; i++) {
            g_s.setDp0Indexed(i, g_s.getDp0Indexed((160 + i)));
        }
    }

    private void Gsm_Preprocess() {
        int index = 0, so_index = 0;
        short z1 = g_s.getZ1();
        int L_z2 = g_s.getL_z2();
        int mp = g_s.getMp();
        short s1 = 0, msp = 0, lsp = 0, SO = 0;
        int L_s2 = 0, L_temp = 0;
        int k = 160;
        while (k != 0) {
            k--;
            SO = (short) (Add.SASR((short) input_signal[index++], (short) 3) << 2);
            s1 = (short) (SO - z1);
            z1 = SO;
            L_s2 = s1;
            L_s2 <<= 15;
            msp = Add.SASR(L_z2, 15);
            lsp = (short) (L_z2 - ((int) (msp << 15)));
            L_s2 += Add.GSM_MULT_R(lsp, (short) 32735);
            L_temp = (int) msp * 32735;
            L_z2 = Add.GSM_L_ADD(L_temp, L_s2);
            L_temp = Add.GSM_L_ADD(L_z2, 16384);
            msp = Add.GSM_MULT_R((short) mp, (short) -28180);
            mp = Add.SASR(L_temp, 15);
            so[so_index++] = Add.GSM_ADD((short) mp, msp);
        }
        g_s.setZ1(z1);
        g_s.setL_z2(L_z2);
        g_s.setMp(mp);
    }
}
