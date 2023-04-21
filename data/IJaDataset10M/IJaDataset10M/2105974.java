package org.rec;

import java.io.Writer;
import org.sodbeans.phonemic.*;
import org.sodbeans.phonemic.tts.*;
import java.util.Iterator;

/**
 * This class represents a DFA capable of recognizing Phonemic protocol
 * messages. After parsing the message, the appropriate Phonemic call is
 * executed using the provided TextToSpeech instance. Protocol messages
 * may be written back to the client using the required Writer instance.
 * 
 * @author jeff
 */
public class Recognizer {

    private static byte[] init__phonemic_actions_0() {
        return new byte[] { 0, 1, 0, 1, 1, 1, 2, 1, 3, 1, 4, 1, 5, 1, 6, 1, 7, 1, 8, 1, 9, 1, 10, 1, 11, 1, 12, 1, 13, 1, 14, 1, 15, 1, 16, 1, 17, 1, 18, 1, 19, 1, 20, 1, 21, 1, 22, 1, 23, 1, 24, 1, 25, 1, 27, 1, 28, 1, 29, 1, 30, 1, 31, 1, 32, 1, 33, 1, 34, 1, 35, 1, 36, 1, 37, 1, 38, 1, 39, 1, 40, 1, 41, 1, 49, 1, 50, 2, 42, 26, 2, 43, 26, 2, 44, 26, 2, 45, 26, 2, 46, 26, 2, 47, 26, 2, 48, 26 };
    }

    private static final byte _phonemic_actions[] = init__phonemic_actions_0();

    private static short[] init__phonemic_key_offsets_0() {
        return new short[] { 0, 0, 6, 7, 8, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 27, 28, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 57, 58, 59, 60, 61, 62, 63, 64, 65, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126, 127, 128, 129, 131, 132, 133, 134, 135, 136, 137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148, 149, 151, 152, 153, 154, 155, 156, 157, 158, 159, 160, 162, 163, 164, 165, 166, 167, 170, 171, 175, 176, 177, 178, 179, 180, 182, 184, 185, 186, 187, 189, 190, 191, 192, 193, 194, 195, 196, 197, 198, 200, 201, 202, 203, 204, 205, 206, 207, 208, 210, 212, 213, 214, 215, 216, 217, 218, 219, 220, 221, 222, 223, 224, 225, 226, 227, 228, 229, 230, 235, 236, 237, 238, 239, 240, 242, 243, 244, 245, 246, 247, 248, 249, 250, 251, 252, 253, 254, 255, 256, 257, 258, 259, 260, 261, 262, 263, 264, 265, 267, 268, 269, 270, 271, 272, 273, 274, 275, 276, 277, 278, 279, 280, 281, 282, 283, 284, 285, 286, 287, 288, 290, 291, 292, 293, 294, 295, 296, 297, 298, 299, 299, 300, 301, 302, 303, 305, 307, 308, 309, 310, 312, 316, 317, 318, 319, 320, 321, 322, 323, 324, 326, 327, 328, 329, 330, 331, 332, 333, 334, 335, 335, 336, 337, 338, 339, 340, 341, 343, 344, 345, 346, 347, 349, 350, 351, 352, 353, 354, 355, 356, 358, 360, 361, 362, 363, 364, 365, 366, 367, 368, 369, 370, 371, 372, 373, 377, 378, 379, 380, 381, 382, 383, 384, 385, 387, 388, 389, 390, 391, 392, 393, 394, 395, 396, 396, 397, 398, 399, 400, 401, 402, 404, 405, 406, 407, 408, 410, 411, 412, 413, 414, 415, 416, 417, 419, 421, 422, 423, 424, 425, 426, 427, 428, 428, 428, 428, 428, 428, 428, 428, 428, 428, 428, 428, 428, 428, 428, 428, 428, 428, 428, 428, 428, 428, 429, 431, 431, 431, 432, 434, 434, 434, 434, 434, 434, 434, 434, 434, 435, 437, 437, 437 };
    }

    private static final short _phonemic_key_offsets[] = init__phonemic_key_offsets_0();

    private static char[] init__phonemic_trans_keys_0() {
        return new char[] { 99, 103, 105, 112, 114, 115, 97, 110, 66, 80, 82, 83, 108, 111, 99, 107, 97, 117, 115, 101, 101, 115, 117, 109, 101, 101, 116, 116, 80, 83, 86, 105, 116, 99, 104, 112, 101, 101, 100, 111, 105, 108, 99, 101, 117, 109, 101, 111, 112, 101, 116, 65, 67, 80, 83, 84, 86, 118, 97, 105, 108, 97, 98, 108, 101, 69, 86, 110, 103, 105, 110, 101, 115, 111, 105, 99, 101, 115, 117, 114, 114, 101, 110, 116, 86, 111, 105, 99, 101, 105, 116, 99, 104, 112, 101, 101, 100, 101, 120, 116, 84, 111, 83, 112, 101, 101, 99, 104, 69, 110, 103, 105, 110, 101, 101, 111, 114, 115, 105, 111, 110, 108, 117, 109, 101, 115, 83, 112, 101, 97, 101, 107, 105, 110, 103, 99, 104, 69, 110, 97, 98, 108, 101, 100, 97, 117, 115, 101, 101, 105, 115, 110, 105, 116, 105, 97, 108, 105, 122, 101, 112, 117, 101, 97, 107, 109, 101, 101, 112, 116, 116, 80, 83, 84, 86, 105, 116, 99, 104, 58, 48, 49, 48, 57, 112, 101, 101, 99, 100, 104, 69, 110, 97, 98, 108, 101, 100, 58, 102, 116, 97, 108, 115, 101, 114, 117, 101, 58, 48, 49, 48, 57, 101, 120, 116, 84, 111, 83, 112, 101, 101, 99, 104, 69, 110, 103, 105, 110, 101, 58, 65, 74, 77, 78, 83, 80, 80, 76, 69, 95, 67, 83, 65, 82, 66, 79, 78, 65, 89, 65, 87, 83, 73, 67, 82, 79, 83, 79, 70, 84, 95, 83, 65, 80, 73, 85, 86, 76, 76, 68, 65, 80, 69, 69, 67, 72, 95, 68, 73, 83, 80, 65, 84, 67, 72, 69, 82, 111, 105, 108, 99, 101, 58, 101, 110, 95, 85, 83, 58, 117, 109, 101, 58, 48, 49, 48, 57, 101, 97, 107, 58, 66, 98, 104, 108, 109, 108, 111, 99, 107, 105, 110, 103, 58, 99, 116, 104, 97, 114, 97, 99, 116, 101, 114, 58, 101, 120, 116, 105, 103, 104, 58, 101, 115, 116, 111, 119, 58, 101, 115, 116, 101, 100, 105, 117, 109, 58, 95, 104, 108, 105, 103, 104, 111, 119, 108, 111, 99, 107, 105, 110, 103, 58, 98, 104, 108, 109, 108, 111, 99, 107, 105, 110, 103, 58, 99, 116, 104, 97, 114, 97, 99, 116, 101, 114, 58, 101, 120, 116, 105, 103, 104, 58, 101, 115, 116, 111, 119, 58, 101, 115, 116, 101, 100, 105, 117, 109, 58, 95, 104, 108, 105, 103, 104, 111, 119, 111, 112, 46, 48, 57, 46, 48, 57, 46, 48, 57, 0 };
    }

    private static final char _phonemic_trans_keys[] = init__phonemic_trans_keys_0();

    private static byte[] init__phonemic_single_lengths_0() {
        return new byte[] { 0, 6, 1, 1, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 6, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 3, 1, 4, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 5, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 2, 4, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0 };
    }

    private static final byte _phonemic_single_lengths[] = init__phonemic_single_lengths_0();

    private static byte[] init__phonemic_range_lengths_0() {
        return new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0 };
    }

    private static final byte _phonemic_range_lengths[] = init__phonemic_range_lengths_0();

    private static short[] init__phonemic_index_offsets_0() {
        return new short[] { 0, 0, 7, 9, 11, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 42, 45, 47, 51, 53, 55, 57, 59, 61, 63, 65, 67, 69, 72, 74, 76, 78, 80, 82, 84, 86, 88, 90, 97, 99, 101, 103, 105, 107, 109, 111, 113, 116, 118, 120, 122, 124, 126, 128, 130, 132, 134, 136, 138, 140, 142, 144, 146, 148, 150, 152, 154, 156, 158, 160, 162, 164, 166, 168, 170, 172, 174, 176, 178, 180, 182, 184, 186, 188, 190, 192, 194, 196, 198, 200, 202, 204, 206, 208, 210, 213, 215, 217, 219, 221, 223, 225, 227, 229, 231, 233, 235, 237, 239, 242, 244, 246, 248, 250, 252, 254, 256, 258, 260, 262, 264, 266, 268, 270, 272, 274, 276, 278, 281, 283, 285, 287, 289, 291, 293, 295, 297, 299, 302, 304, 306, 308, 310, 312, 316, 318, 323, 325, 327, 329, 331, 333, 335, 337, 339, 341, 343, 346, 348, 350, 352, 354, 356, 358, 360, 362, 364, 367, 369, 371, 373, 375, 377, 379, 381, 383, 385, 387, 389, 391, 393, 395, 397, 399, 401, 403, 405, 407, 409, 411, 413, 415, 417, 419, 421, 423, 429, 431, 433, 435, 437, 439, 442, 444, 446, 448, 450, 452, 454, 456, 458, 460, 462, 464, 466, 468, 470, 472, 474, 476, 478, 480, 482, 484, 486, 488, 491, 493, 495, 497, 499, 501, 503, 505, 507, 509, 511, 513, 515, 517, 519, 521, 523, 525, 527, 529, 531, 533, 536, 538, 540, 542, 544, 546, 548, 550, 552, 554, 555, 557, 559, 561, 563, 565, 567, 569, 571, 573, 576, 581, 583, 585, 587, 589, 591, 593, 595, 597, 600, 602, 604, 606, 608, 610, 612, 614, 616, 618, 619, 621, 623, 625, 627, 629, 631, 634, 636, 638, 640, 642, 645, 647, 649, 651, 653, 655, 657, 659, 662, 665, 667, 669, 671, 673, 675, 677, 679, 681, 683, 685, 687, 689, 691, 696, 698, 700, 702, 704, 706, 708, 710, 712, 715, 717, 719, 721, 723, 725, 727, 729, 731, 733, 734, 736, 738, 740, 742, 744, 746, 749, 751, 753, 755, 757, 760, 762, 764, 766, 768, 770, 772, 774, 777, 780, 782, 784, 786, 788, 790, 792, 794, 795, 796, 797, 798, 799, 800, 801, 802, 803, 804, 805, 806, 807, 808, 809, 810, 811, 812, 813, 814, 815, 817, 819, 820, 821, 823, 825, 826, 827, 828, 829, 830, 831, 832, 833, 835, 837, 838, 839 };
    }

    private static final short _phonemic_index_offsets[] = init__phonemic_index_offsets_0();

    private static short[] init__phonemic_trans_targs_0() {
        return new short[] { 2, 38, 107, 125, 129, 146, 0, 3, 0, 4, 0, 5, 9, 13, 18, 0, 6, 0, 7, 0, 8, 0, 373, 0, 10, 0, 11, 0, 12, 0, 374, 0, 14, 0, 15, 0, 16, 0, 17, 0, 375, 0, 19, 36, 0, 20, 0, 21, 25, 29, 0, 22, 0, 23, 0, 24, 0, 376, 0, 26, 0, 27, 0, 28, 0, 377, 0, 30, 0, 31, 33, 0, 32, 0, 378, 0, 34, 0, 35, 0, 379, 0, 37, 0, 380, 0, 39, 0, 40, 0, 41, 61, 72, 76, 80, 97, 0, 42, 0, 43, 0, 44, 0, 45, 0, 46, 0, 47, 0, 48, 0, 49, 0, 50, 56, 0, 51, 0, 52, 0, 53, 0, 54, 0, 55, 0, 381, 0, 57, 0, 58, 0, 59, 0, 60, 0, 382, 0, 62, 0, 63, 0, 64, 0, 65, 0, 66, 0, 67, 0, 68, 0, 69, 0, 70, 0, 71, 0, 383, 0, 73, 0, 74, 0, 75, 0, 384, 0, 77, 0, 78, 0, 79, 0, 384, 0, 81, 0, 82, 0, 83, 0, 84, 0, 85, 0, 86, 0, 87, 0, 88, 0, 89, 0, 90, 0, 91, 0, 92, 0, 93, 0, 94, 0, 95, 0, 96, 0, 385, 0, 98, 103, 0, 99, 0, 100, 0, 101, 0, 102, 0, 386, 0, 104, 0, 105, 0, 106, 0, 387, 0, 108, 0, 109, 0, 110, 0, 111, 0, 112, 116, 0, 113, 0, 114, 0, 115, 0, 388, 0, 117, 0, 118, 0, 119, 0, 120, 0, 121, 0, 122, 0, 123, 0, 124, 0, 389, 0, 126, 0, 127, 0, 128, 0, 390, 0, 130, 0, 131, 140, 0, 132, 0, 133, 0, 134, 0, 135, 0, 136, 0, 137, 0, 138, 0, 139, 0, 391, 0, 141, 144, 0, 142, 0, 143, 0, 392, 0, 145, 0, 393, 0, 147, 267, 371, 0, 148, 0, 149, 156, 180, 249, 0, 150, 0, 151, 0, 152, 0, 153, 0, 154, 0, 394, 0, 395, 0, 157, 0, 158, 0, 159, 0, 160, 177, 0, 161, 0, 162, 0, 163, 0, 164, 0, 165, 0, 166, 0, 167, 0, 168, 0, 169, 0, 170, 174, 0, 171, 0, 172, 0, 173, 0, 396, 0, 175, 0, 176, 0, 397, 0, 178, 0, 398, 0, 399, 0, 181, 0, 182, 0, 183, 0, 184, 0, 185, 0, 186, 0, 187, 0, 188, 0, 189, 0, 190, 0, 191, 0, 192, 0, 193, 0, 194, 0, 195, 0, 196, 0, 197, 0, 198, 0, 199, 212, 215, 228, 233, 0, 200, 0, 201, 0, 202, 0, 203, 0, 204, 0, 205, 210, 0, 206, 0, 207, 0, 208, 0, 209, 0, 400, 0, 211, 0, 401, 0, 213, 0, 214, 0, 402, 0, 216, 0, 217, 0, 218, 0, 219, 0, 220, 0, 221, 0, 222, 0, 223, 0, 224, 0, 225, 0, 226, 0, 227, 0, 403, 0, 229, 231, 0, 230, 0, 404, 0, 232, 0, 405, 0, 234, 0, 235, 0, 236, 0, 237, 0, 238, 0, 239, 0, 240, 0, 241, 0, 242, 0, 243, 0, 244, 0, 245, 0, 246, 0, 247, 0, 248, 0, 406, 0, 250, 0, 251, 261, 0, 252, 0, 253, 0, 254, 0, 255, 0, 256, 0, 257, 0, 258, 0, 259, 0, 260, 0, 407, 262, 0, 263, 0, 264, 0, 265, 0, 408, 0, 409, 0, 268, 0, 269, 0, 270, 0, 271, 317, 0, 272, 294, 300, 305, 0, 273, 0, 274, 0, 275, 0, 276, 0, 277, 0, 278, 0, 279, 0, 280, 0, 281, 291, 0, 282, 0, 283, 0, 284, 0, 285, 0, 286, 0, 287, 0, 288, 0, 289, 0, 290, 0, 410, 292, 0, 293, 0, 289, 0, 295, 0, 296, 0, 297, 0, 280, 298, 0, 299, 0, 279, 0, 301, 0, 302, 0, 280, 303, 0, 304, 0, 279, 0, 306, 0, 307, 0, 308, 0, 309, 0, 310, 0, 280, 311, 0, 312, 315, 0, 313, 0, 314, 0, 279, 0, 316, 0, 279, 0, 318, 0, 319, 0, 320, 0, 321, 0, 322, 0, 323, 0, 324, 0, 325, 0, 326, 348, 354, 359, 0, 327, 0, 328, 0, 329, 0, 330, 0, 331, 0, 332, 0, 333, 0, 334, 0, 335, 345, 0, 336, 0, 337, 0, 338, 0, 339, 0, 340, 0, 341, 0, 342, 0, 343, 0, 344, 0, 411, 346, 0, 347, 0, 343, 0, 349, 0, 350, 0, 351, 0, 334, 352, 0, 353, 0, 333, 0, 355, 0, 356, 0, 334, 357, 0, 358, 0, 333, 0, 360, 0, 361, 0, 362, 0, 363, 0, 364, 0, 334, 365, 0, 366, 369, 0, 367, 0, 368, 0, 333, 0, 370, 0, 333, 0, 372, 0, 412, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 155, 0, 395, 0, 0, 0, 179, 0, 399, 0, 0, 0, 0, 0, 0, 0, 0, 407, 266, 0, 409, 0, 410, 411, 0, 0 };
    }

    private static final short _phonemic_trans_targs[] = init__phonemic_trans_targs_0();

    private static byte[] init__phonemic_trans_actions_0() {
        return new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 61, 0, 61, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 61, 0, 61, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 81, 0, 0, 0, 0, 0, 0, 0, 0, 61, 0, 61, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 65, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 85, 0, 0, 0, 63, 0, 0, 0, 0, 83, 0, 0, 0, 0, 0, 69, 0, 0, 0, 0, 0, 0, 67, 0, 0, 0, 77, 0, 0, 0, 0, 0, 0, 79, 0, 0, 0, 0, 0, 0, 0, 0, 0, 73, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 71, 0, 0, 0, 75, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 65, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 85, 0, 0, 0, 63, 0, 0, 0, 0, 83, 0, 0, 0, 0, 0, 69, 0, 0, 0, 0, 0, 0, 67, 0, 0, 0, 77, 0, 0, 0, 0, 0, 0, 79, 0, 0, 0, 0, 0, 0, 0, 0, 0, 73, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 71, 0, 0, 0, 75, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 61, 0, 61, 0, 0, 0, 61, 0, 61, 0, 0, 0, 0, 0, 0, 0, 0, 81, 61, 0, 61, 0, 63, 63, 0, 0 };
    }

    private static final byte _phonemic_trans_actions[] = init__phonemic_trans_actions_0();

    private static byte[] init__phonemic_eof_actions_0() {
        return new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 35, 37, 39, 41, 43, 45, 47, 49, 53, 11, 1, 21, 51, 59, 17, 27, 29, 13, 55, 57, 15, 25, 25, 33, 31, 23, 23, 96, 99, 87, 93, 105, 90, 102, 9, 19, 19, 3, 5, 7 };
    }

    private static final byte _phonemic_eof_actions[] = init__phonemic_eof_actions_0();

    static final int phonemic_start = 1;

    static final int phonemic_first_final = 373;

    static final int phonemic_error = 0;

    static final int phonemic_en_main = 1;

    private TextToSpeech textToSpeech = null;

    public Recognizer(TextToSpeech textToSpeech) {
        this.textToSpeech = textToSpeech;
    }

    public void setTextToSpeech(TextToSpeech textToSpeech) {
        this.textToSpeech = textToSpeech;
    }

    public TextToSpeech getTextToSpeech() {
        return this.textToSpeech;
    }

    /**
     * Parse the given input packet and execute the command given. If any text
     * is to be written back to the client, it will be returned. If no text is
     * to be written, null is returned.
     */
    public String execute(String input) {
        int cs = 0;
        int p = 0;
        int pe = input.length();
        int eof = input.length();
        char[] data = input.toCharArray();
        StringBuilder textToSpeak = new StringBuilder();
        StringBuilder decimalString = new StringBuilder();
        StringBuilder voiceName = new StringBuilder();
        RequestType requestType = RequestType.TEXT;
        SpeechPriority priority = SpeechPriority.MEDIUM;
        TextToSpeechEngine engine = textToSpeech.getTextToSpeechEngine();
        String response = null;
        {
            cs = phonemic_start;
        }
        {
            int _klen;
            int _trans = 0;
            int _acts;
            int _nacts;
            int _keys;
            int _goto_targ = 0;
            _goto: while (true) {
                switch(_goto_targ) {
                    case 0:
                        if (p == pe) {
                            _goto_targ = 4;
                            continue _goto;
                        }
                        if (cs == 0) {
                            _goto_targ = 5;
                            continue _goto;
                        }
                    case 1:
                        _match: do {
                            _keys = _phonemic_key_offsets[cs];
                            _trans = _phonemic_index_offsets[cs];
                            _klen = _phonemic_single_lengths[cs];
                            if (_klen > 0) {
                                int _lower = _keys;
                                int _mid;
                                int _upper = _keys + _klen - 1;
                                while (true) {
                                    if (_upper < _lower) break;
                                    _mid = _lower + ((_upper - _lower) >> 1);
                                    if (data[p] < _phonemic_trans_keys[_mid]) _upper = _mid - 1; else if (data[p] > _phonemic_trans_keys[_mid]) _lower = _mid + 1; else {
                                        _trans += (_mid - _keys);
                                        break _match;
                                    }
                                }
                                _keys += _klen;
                                _trans += _klen;
                            }
                            _klen = _phonemic_range_lengths[cs];
                            if (_klen > 0) {
                                int _lower = _keys;
                                int _mid;
                                int _upper = _keys + (_klen << 1) - 2;
                                while (true) {
                                    if (_upper < _lower) break;
                                    _mid = _lower + (((_upper - _lower) >> 1) & ~1);
                                    if (data[p] < _phonemic_trans_keys[_mid]) _upper = _mid - 2; else if (data[p] > _phonemic_trans_keys[_mid + 1]) _lower = _mid + 2; else {
                                        _trans += ((_mid - _keys) >> 1);
                                        break _match;
                                    }
                                }
                                _trans += _klen;
                            }
                        } while (false);
                        cs = _phonemic_trans_targs[_trans];
                        if (_phonemic_trans_actions[_trans] != 0) {
                            _acts = _phonemic_trans_actions[_trans];
                            _nacts = (int) _phonemic_actions[_acts++];
                            while (_nacts-- > 0) {
                                switch(_phonemic_actions[_acts++]) {
                                    case 31:
                                        {
                                            decimalString.append(data[p]);
                                        }
                                        break;
                                    case 32:
                                        {
                                            textToSpeak.append(data[p]);
                                        }
                                        break;
                                    case 33:
                                        {
                                            priority = SpeechPriority.BLOCKING;
                                        }
                                        break;
                                    case 34:
                                        {
                                            priority = SpeechPriority.HIGHEST;
                                        }
                                        break;
                                    case 35:
                                        {
                                            priority = SpeechPriority.HIGH;
                                        }
                                        break;
                                    case 36:
                                        {
                                            priority = SpeechPriority.MEDIUM_HIGH;
                                        }
                                        break;
                                    case 37:
                                        {
                                            priority = SpeechPriority.MEDIUM;
                                        }
                                        break;
                                    case 38:
                                        {
                                            priority = SpeechPriority.MEDIUM_LOW;
                                        }
                                        break;
                                    case 39:
                                        {
                                            priority = SpeechPriority.LOW;
                                        }
                                        break;
                                    case 40:
                                        {
                                            priority = SpeechPriority.LOWEST;
                                        }
                                        break;
                                    case 41:
                                        {
                                            voiceName.append(data[p]);
                                        }
                                        break;
                                    case 49:
                                        {
                                            requestType = RequestType.TEXT;
                                        }
                                        break;
                                    case 50:
                                        {
                                            requestType = RequestType.CHARACTER;
                                        }
                                        break;
                                }
                            }
                        }
                    case 2:
                        if (cs == 0) {
                            _goto_targ = 5;
                            continue _goto;
                        }
                        if (++p != pe) {
                            _goto_targ = 1;
                            continue _goto;
                        }
                    case 4:
                        if (p == eof) {
                            int __acts = _phonemic_eof_actions[cs];
                            int __nacts = (int) _phonemic_actions[__acts++];
                            while (__nacts-- > 0) {
                                switch(_phonemic_actions[__acts++]) {
                                    case 0:
                                        {
                                            SpeechVoice v = textToSpeech.getCurrentVoice();
                                            if (v == null) response = "null"; else response = v.toString();
                                        }
                                        break;
                                    case 1:
                                        {
                                            boolean phonemicResult = textToSpeech.speak(textToSpeak.toString(), priority, requestType);
                                            response = Boolean.toString(phonemicResult);
                                        }
                                        break;
                                    case 2:
                                        {
                                            boolean phonemicResult = textToSpeech.speakBlocking(textToSpeak.toString(), priority, requestType);
                                            response = Boolean.toString(phonemicResult);
                                        }
                                        break;
                                    case 3:
                                        {
                                            boolean phonemicResult = textToSpeech.stop();
                                            response = Boolean.toString(phonemicResult);
                                        }
                                        break;
                                    case 4:
                                        {
                                            SpeechVoice voice = new SpeechVoice(voiceName.toString(), SpeechLanguage.ENGLISH_US);
                                            boolean phonemicResult = textToSpeech.setVoice(voice);
                                            response = Boolean.toString(phonemicResult);
                                        }
                                        break;
                                    case 5:
                                        {
                                            Iterator<SpeechVoice> voices = textToSpeech.getAvailableVoices();
                                            StringBuilder voicesString = new StringBuilder();
                                            while (voices.hasNext()) {
                                                voicesString.append(voices.next().toString());
                                                voicesString.append('\n');
                                            }
                                            response = voicesString.toString();
                                        }
                                        break;
                                    case 6:
                                        {
                                            boolean phonemicResult = textToSpeech.pause();
                                            response = Boolean.toString(phonemicResult);
                                        }
                                        break;
                                    case 7:
                                        {
                                            boolean phonemicResult = textToSpeech.resume();
                                            response = Boolean.toString(phonemicResult);
                                        }
                                        break;
                                    case 8:
                                        {
                                            response = Double.toString(textToSpeech.getVolume());
                                        }
                                        break;
                                    case 9:
                                        {
                                            double vol = Double.parseDouble(decimalString.toString());
                                            System.out.println("decimalString = " + decimalString + " VOL = " + vol);
                                            boolean phonemicResult = textToSpeech.setVolume(vol);
                                            response = Boolean.toString(phonemicResult);
                                        }
                                        break;
                                    case 10:
                                        {
                                            response = Double.toString(textToSpeech.getSpeed());
                                        }
                                        break;
                                    case 11:
                                        {
                                            double speed = Double.parseDouble(decimalString.toString());
                                            boolean phonemicResult = textToSpeech.setSpeed(speed);
                                            response = Boolean.toString(phonemicResult);
                                        }
                                        break;
                                    case 12:
                                        {
                                            double pitch = Double.parseDouble(decimalString.toString());
                                            boolean phonemicResult = textToSpeech.setPitch(pitch);
                                            response = Boolean.toString(phonemicResult);
                                        }
                                        break;
                                    case 13:
                                        {
                                            response = Boolean.toString(textToSpeech.isSpeaking());
                                        }
                                        break;
                                    case 14:
                                        {
                                            response = Boolean.toString(textToSpeech.isSpeechEnabled());
                                        }
                                        break;
                                    case 15:
                                        {
                                            textToSpeech.setSpeechEnabled(true);
                                        }
                                        break;
                                    case 16:
                                        {
                                            textToSpeech.setSpeechEnabled(false);
                                        }
                                        break;
                                    case 17:
                                        {
                                            boolean phonemicResult = textToSpeech.canBlock();
                                            response = Boolean.toString(phonemicResult);
                                        }
                                        break;
                                    case 18:
                                        {
                                            boolean phonemicResult = textToSpeech.canPause();
                                            response = Boolean.toString(phonemicResult);
                                        }
                                        break;
                                    case 19:
                                        {
                                            boolean phonemicResult = textToSpeech.canResume();
                                            response = Boolean.toString(phonemicResult);
                                        }
                                        break;
                                    case 20:
                                        {
                                            boolean phonemicResult = textToSpeech.canSetPitch();
                                            response = Boolean.toString(phonemicResult);
                                        }
                                        break;
                                    case 21:
                                        {
                                            boolean phonemicResult = textToSpeech.canSetSpeed();
                                            response = Boolean.toString(phonemicResult);
                                        }
                                        break;
                                    case 22:
                                        {
                                            boolean phonemicResult = textToSpeech.canSetVoice();
                                            response = Boolean.toString(phonemicResult);
                                        }
                                        break;
                                    case 23:
                                        {
                                            boolean phonemicResult = textToSpeech.canSetVolume();
                                            response = Boolean.toString(phonemicResult);
                                        }
                                        break;
                                    case 24:
                                        {
                                            boolean phonemicResult = textToSpeech.canStop();
                                            response = Boolean.toString(phonemicResult);
                                        }
                                        break;
                                    case 25:
                                        {
                                            TextToSpeechEngine currentEngine = textToSpeech.getTextToSpeechEngine();
                                            response = currentEngine.toString();
                                        }
                                        break;
                                    case 26:
                                        {
                                            boolean phonemicResult = textToSpeech.setTextToSpeechEngine(engine);
                                            response = Boolean.toString(phonemicResult);
                                        }
                                        break;
                                    case 27:
                                        {
                                            Iterator<TextToSpeechEngine> engines = textToSpeech.getAvailableEngines();
                                            StringBuilder enginesString = new StringBuilder();
                                            while (engines.hasNext()) {
                                                enginesString.append(engines.next().toString());
                                                enginesString.append('\n');
                                            }
                                            response = enginesString.toString();
                                        }
                                        break;
                                    case 28:
                                        {
                                            textToSpeech.reinitialize();
                                        }
                                        break;
                                    case 29:
                                        {
                                            boolean phonemicResult = textToSpeech.respeak();
                                            response = Boolean.toString(phonemicResult);
                                        }
                                        break;
                                    case 30:
                                        {
                                        }
                                        break;
                                    case 42:
                                        {
                                            engine = TextToSpeechEngine.JAWS;
                                        }
                                        break;
                                    case 43:
                                        {
                                            engine = TextToSpeechEngine.NVDA;
                                        }
                                        break;
                                    case 44:
                                        {
                                            engine = TextToSpeechEngine.MICROSOFT_SAPI;
                                        }
                                        break;
                                    case 45:
                                        {
                                            engine = TextToSpeechEngine.APPLE_CARBON;
                                        }
                                        break;
                                    case 46:
                                        {
                                            engine = TextToSpeechEngine.APPLE_SAY;
                                        }
                                        break;
                                    case 47:
                                        {
                                            engine = TextToSpeechEngine.SPEECH_DISPATCHER;
                                        }
                                        break;
                                    case 48:
                                        {
                                            engine = TextToSpeechEngine.NULL;
                                        }
                                        break;
                                }
                            }
                        }
                    case 5:
                }
                break;
            }
        }
        if (p != pe) {
            response = "error";
        }
        if (response != null) response = response + "\n";
        return response;
    }

    /**
     * Write a response to the client.
     */
    private void write(String text) {
    }
}
